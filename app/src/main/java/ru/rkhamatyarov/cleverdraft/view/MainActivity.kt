package ru.rkhamatyarov.cleverdraft.view

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
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
import ru.rkhamatyarov.cleverdraft.ChiefApp
import ru.rkhamatyarov.cleverdraft.MainMVP
import javax.inject.Inject

import ru.rkhamatyarov.cleverdraft.MainMVP.ProvidedPresenterOps
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter

import ru.rkhamatyarov.cleverdraft.utilities.StateMaintainer
import ru.rkhamatyarov.cleverdraft.utililities.di.*
import ru.rkhamatyarov.cleverdraft.utilities.di.module.DateTimePickerFragmentModule

class MainActivity : AppCompatActivity(), View.OnClickListener, MainMVP.ViewOps {
    private var mainTextNewNote: EditText? = null
//    private var mainListAdapter: ListNotes? = null
    private var mProgress: ProgressBar? = null
    private var toolbar: Toolbar? = null
    override fun showAlert(alertDialog: android.app.AlertDialog) = alertDialog.show()



    @Inject
    lateinit var mainPresenter: MainMVP.ProvidedPresenterOps

    @Inject
    lateinit var dateTimePickerPresenter: DateTimePickerPresenter

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


        val fab = findViewById(R.id.fab)
        fab.setOnClickListener(this)

        mainTextNewNote = findViewById(R.id.edit_note) as EditText

        insertIntentText()

        mProgress = findViewById(R.id.progressbar) as ProgressBar?


        toolbar = findViewById(R.id.main_toolbar) as? Toolbar // Attaching the layout to the toolbar object
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
        setupComponent()
    }


    private fun reinitialize() {
        mainPresenter = mStateMaintainer[MainPresenter::class.java.simpleName] as ProvidedPresenterOps
        mainPresenter.setView(this)
    }


    private fun setupComponent() {
        ChiefApp.get(this).getAppComponent().getMainComponent(MainActivityModule(this)).inject(this)

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
        ChiefApp.get(this).getAppComponent().getDateTimePickerComponent(DateTimePickerFragmentModule(dateTimePickerFragment)).inject(dateTimePickerFragment)
        dateTimePickerPresenter.setDateTimeToPicker(fragmentManager)

    }

}