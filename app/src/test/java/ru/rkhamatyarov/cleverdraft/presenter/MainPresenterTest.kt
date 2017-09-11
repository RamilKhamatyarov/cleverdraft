package ru.rkhamatyarov.cleverdraft.presenter

import android.widget.EditText
import android.widget.Toast
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.*

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.rkhamatyarov.cleverdraft.BuildConfig
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.MainModel
import ru.rkhamatyarov.cleverdraft.model.Note

/**
 * Created by Asus on 03.09.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class MainPresenterTest {
    private lateinit var mainPresenter: MainPresenter
    private lateinit var mockModel: MainModel
    private lateinit var mockView: MainMVP.ViewOps

    @Before
    fun setup(){
        mockView = Mockito.mock( MainMVP.ViewOps::class.java )
        mockModel = Mockito.mock( MainModel::class.java, RETURNS_DEEP_STUBS)
        mainPresenter = MainPresenter(mockView)
        mainPresenter.setModel(mockModel)

        `when`(mockModel.loadData()).thenReturn(true)
        reset(mockView)
    }

    @Test
    fun testClickNewNote(){
        val mockEditText: EditText = mock(EditText::class.java, RETURNS_DEEP_STUBS)
        `when`(mockEditText.text.toString()).thenReturn("Test_true")
        val arrayPosition: Int = 10

        `when`(mockModel.insertNote(any())).thenReturn(arrayPosition)

        mainPresenter.clickNewNote(mockEditText)


        verify(mockModel).insertNote(any());
        verify(mockView).notifyItemInserted(eq(arrayPosition.inc()))
        verify(mockView).notifyItemRangeChanged(eq(arrayPosition), anyInt())
        verify(mockView, never())!!.showToast(any())

    }

    //https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T

}