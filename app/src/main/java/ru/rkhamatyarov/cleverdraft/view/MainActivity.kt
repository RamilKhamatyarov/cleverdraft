package ru.rkhamatyarov.cleverdraft.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.orm.SugarContext
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.R

class MainActivity : AppCompatActivity(), View.OnClickListener, MainMVP.ViewOps {
    private lateinit var btnAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupMVP()
    }

    private fun setupViews() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        btnAdd = findViewById(R.id.fab) as FloatingActionButton
        btnAdd.setOnClickListener(this)

        TODO("Implement recycle view")
    }

    private fun setupMVP() {
        TODO("Implement main MVP")
    }

    override fun onClick(view: View) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        val intent: Intent

        when (view.id) {
            R.id.fab -> {
                intent = Intent(this, TextEditActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun getAppContext(): Context = getApplicationContext();
    override fun getActContext(): Context = this

    override fun showToast(toast: Toast) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAlert(alertDialog: AlertDialog) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyItemRemoved(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyDataSetChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyItemInserted(layoutPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyItemRangeChanged(startPosition: Int, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearEditText() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
