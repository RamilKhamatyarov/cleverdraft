package ru.rkhamatyarov.cleverdraft.view


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import org.koin.android.ext.android.inject
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.utilities.StateMaintainer

class MainActivity : AppCompatActivity(), View.OnClickListener, MainMVP.ViewOps {
    private var mainTextNewNote: EditText? = null
//    private var mainListAdapter: ListNotes? = null
    private var mProgress: ProgressBar? = null
    private var toolbar: Toolbar? = null
    override fun showAlert(alertDialog: android.app.AlertDialog) = alertDialog.show()

    val mainPresenter: MainMVP.ProvidedPresenterOps by inject()

    val dateTimePickerPresenter: DateTimePickerPresenter by inject()

    // Responsible to maintain the object's integrity
    // during configurations change
    private val mStateMaintainer = StateMaintainer(fragmentManager, MainActivity::class.java.name)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMVP()
        setContentView(R.layout.activity_main)

        setupViews()

    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy(isChangingConfigurations)
    }

    /**
     * Setup the Views
     */
    private fun setupViews() {


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(this)

        mainTextNewNote = findViewById<EditText>(R.id.edit_note)

        insertIntentText()

        mProgress = findViewById<ProgressBar>(R.id.progressbar)


        toolbar = findViewById<Toolbar>(R.id.main_toolbar) // Attaching the layout to the toolbar object
        if (toolbar != null) {
            setSupportActionBar(toolbar)  // Setting toolbar as the ActionBar with setSupportActionBar() call
        }


    }

    private fun insertIntentText() {

        val message = intent.getStringExtra(MainPresenter.EXTRA_MESSAGE)
        mainTextNewNote?.setText(message)
    }

    /**
     * Setup Model View Presenter pattern.
     * Use a [StateMaintainer] to maintain the
     * Presenter and Model instances between configuration changes.
     */

    private fun setupMVP() = if (mStateMaintainer.firstTimeIn()) {
        initialize()
    } else {
        reinitialize()
    }


    private fun initialize() {
        Log.d(TAG, "initialize")
        //TODO remove if don't work
    }


    private fun reinitialize() {
        //TODO see work, if not fix it
//        mainPresenter = mStateMaintainer[MainPresenter::class.java.simpleName] as ProvidedPresenterOps
        mainPresenter.setView(this)
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {

                val messageId = intent.getIntExtra(MainPresenter.EXTRA_MESSAGE_ID, -1)

                if (messageId == -1) {
                    mainTextNewNote?.let { mainPresenter.newNote(it) }
                } else {
                    mainTextNewNote?.let { mainPresenter.updateNote(it, messageId) }
                }
                startListActivity()
            }
        }
    }

    override fun showToast(toast: Toast) {
        toast.show()
    }

    override fun setEditText(content: String){
        checkNotNull(mainTextNewNote).setText(content)
    }

    override fun clearEditText() {
        mainTextNewNote?.setText("")
    }

    override fun showProgress() {
        mProgress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgress?.visibility = View.GONE
    }

    fun showAlert(dialog: AlertDialog) {
        dialog.show()
    }

    override fun notifyItemRemoved(position: Int) {
    }

    override fun notifyItemInserted(adapterPos: Int) {

    }

    override fun notifyItemChanged(adapterPos: Int) {

    }

    override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int?) {

    }

    override fun notifyDataSetChanged() {

    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }

    override fun getAppContext(): Context = applicationContext

    override fun getActContext(): Context = this

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val b = when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_navigation -> {
                startListActivity()
                true
            }
            R.id.action_datetime -> {
                setDateTime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return b
    }

    private fun startListActivity(){
        val intent = Intent(this, DraftListActivity::class.java)
        this.startActivity(intent)
    }

    private fun setDateTime() {
        val dateTimePickerFragment = DateTimePickerFragment()
        dateTimePickerPresenter.setDateTimeToPicker(fragmentManager)

    }

}