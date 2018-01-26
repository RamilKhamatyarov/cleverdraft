package ru.rkhamatyarov.cleverdraft.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.ChiefApp
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.utililities.di.MainActivityModule
import ru.rkhamatyarov.cleverdraft.utilities.StateMaintainer
import ru.rkhamatyarov.cleverdraft.utilities.di.DraftListActivityModule
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder
import javax.inject.Inject

/**
 * Created by Asus on 23.07.2017.
 */

class DraftListActivity : AppCompatActivity(), View.OnClickListener, MainMVP.ViewOps  {

    private var mainTextNewNote: EditText? = null
    private var mainListAdapter: ListNotes? = null
    private var mProgress: ProgressBar? = null
    private var toolbar: Toolbar? = null

    override fun showAlert(alertDialog: android.app.AlertDialog) = alertDialog.show()

    @Inject
    lateinit var draftListPresenter: MainMVP.ProvidedPresenterOps

    // Responsible to maintain the object's integrity
    // during configurations change
    private val mStateMaintainer = StateMaintainer(fragmentManager, MainActivity::class.java.name)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMVP()
        setContentView(R.layout.activity_notepad)

        setupViews()

    }

    override fun onDestroy() {
        super.onDestroy()
        draftListPresenter.onDestroy(isChangingConfigurations)
    }

    /**
     * Setup the Views
     */
    private fun setupViews() {

        val fab = findViewById(R.id.list_fab)
        fab.setOnClickListener(this)

        mainListAdapter = ListNotes()
        mProgress = findViewById(R.id.list_progressbar) as ProgressBar?

        val mList = findViewById(R.id.list_notes) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        mList.layoutManager = linearLayoutManager
        mList.adapter = mainListAdapter
        mList.itemAnimator = DefaultItemAnimator()

        toolbar = findViewById(R.id.list_toolbar) as? Toolbar // Attaching the layout to the toolbar object
        if (toolbar != null) {
            setSupportActionBar(toolbar)  // Setting toolbar as the ActionBar with setSupportActionBar() call

            // Get a support ActionBar corresponding to this toolbar
            val ab = supportActionBar

            // Enable the Up button
            ab?.setDisplayHomeAsUpEnabled(true)
        }
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
        draftListPresenter = mStateMaintainer[MainPresenter::class.java.simpleName] as MainMVP.ProvidedPresenterOps
        draftListPresenter.setView(this)
    }


    private fun setupComponent() {
        ChiefApp.get(this).getAppComponent().getDraftListComponent(DraftListActivityModule(this)).inject(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                TODO("not work - fix")
                startMainActivity()
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

    fun showAlert(dialog: android.support.v7.app.AlertDialog) {
        dialog.show()
    }

    override fun notifyItemRemoved(position: Int) {
        mainListAdapter?.notifyItemRemoved(position)
    }

    override fun notifyItemInserted(adapterPos: Int) {
        mainListAdapter?.notifyItemInserted(adapterPos)
    }

    override fun notifyItemChanged(adapterPos: Int) {
        mainListAdapter?.notifyItemChanged(adapterPos)
    }

    override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int?) {
        if (itemCount != null) {
            mainListAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }
    }

    override fun notifyDataSetChanged() {
        mainListAdapter?.notifyDataSetChanged()
    }

    private inner class ListNotes : RecyclerView.Adapter<NotesViewHolder>() {


        override fun getItemCount(): Int {
            val count = draftListPresenter.getNotesCount()
            if (count != null) {
                return count
            }
            return 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
            return draftListPresenter.createViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            draftListPresenter.bindViewHolder(holder, position)
        }

    }

    companion object {

        private val TAG = DraftListActivity::class.java.simpleName
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
            R.id.action_reminder -> {
                draftListPresenter.setDateTimePicker(fragmentManager)
//                val dateTimePicker: DialogFragment = DatePickerFragment()
//                dateTimePicker.show(fragmentManager, "timePicker")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return b
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

}
