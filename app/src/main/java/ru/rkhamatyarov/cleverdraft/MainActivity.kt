package ru.rkhamatyarov.cleverdraft

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import javax.inject.Inject

import ru.rkhamatyarov.cleverdraft.MainMVP.ProvidedPresenterOps
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder

import ru.rkhamatyarov.cleverdraft.utililities.StateMaintainer
import ru.rkhamatyarov.cleverdraft.utililities.di.*

class MainActivity : AppCompatActivity(), View.OnClickListener, MainMVP.ViewOps {
    private var mainTextNewNote: EditText? = null
    private var mainListAdapter: ListNotes? = null
    private var mProgress: ProgressBar? = null

    override fun showAlert(alertDialog: android.app.AlertDialog) = alertDialog.show()



    @Inject
    lateinit var mainPresenter: MainMVP.ProvidedPresenterOps

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
        /*val toolbar:Toolbar? = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)*/

        val fab = findViewById(R.id.fab)
        fab.setOnClickListener(this)

        mainTextNewNote = findViewById(R.id.edit_note) as EditText
        mainListAdapter = ListNotes()
        mProgress = findViewById(R.id.progressbar) as ProgressBar?

        val mList = findViewById(R.id.list_notes) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        mList.layoutManager = linearLayoutManager
        mList.adapter = mainListAdapter
        mList.itemAnimator = DefaultItemAnimator()
    }

    /**
     * Setup Model View Presenter pattern.
     * Use a [StateMaintainer] to maintain the
     * Presenter and Model instances between configuration changes.
     */
    private fun setupMVP() {
        if (mStateMaintainer.firstTimeIn()) {
            initialize()
        } else {
            reinitialize()
        }
    }


    private fun initialize() {
        Log.d(TAG, "initialize")
        setupComponent()
    }


    private fun reinitialize() {
        mainPresenter = mStateMaintainer[MainPresenter::class.java.simpleName] as ProvidedPresenterOps
//        var model: MainMVP.ProvidedModelOps = MainModel(mainPresenter)
        mainPresenter.setView(this)
//        (mainPresenter as MainPresenter).mainModel = model

//        mStateMaintainer.put(mainPresenter)
//        mStateMaintainer.put(model)
//        if (mainPresenter == null)
//            initialize()
    }


    private fun setupComponent() {
        ChiefApp.get(this).getAppComponent().getMainComponent(MainActivityModule(this)).inject(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                // Add new note
                mainTextNewNote?.let { mainPresenter.clickNewNote(it) }
            }
        }
    }

    override fun showToast(toast: Toast) {
        toast.show()
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
        mainListAdapter?.notifyItemRemoved(position)
    }

    override fun notifyItemInserted(adapterPos: Int) {
        mainListAdapter?.notifyItemInserted(adapterPos)
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
            val count = mainPresenter.getNotesCount()
            if (count != null) {
                return count
            }
            return 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
            return mainPresenter.createViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            mainPresenter.bindViewHolder(holder, position)
        }

    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }

    override fun getAppContext(): Context = applicationContext

    override fun getActContext(): Context = this

}