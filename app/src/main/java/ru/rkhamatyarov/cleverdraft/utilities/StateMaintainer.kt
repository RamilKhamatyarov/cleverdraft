package ru.rkhamatyarov.cleverdraft.utilities

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.util.Log
import java.lang.ref.WeakReference


class StateMaintainer (fragmentManager: FragmentManager, private val mainStateMaintenerTag: String) {

    protected val TAG = javaClass.simpleName
    private val mainFragmentManager: WeakReference<FragmentManager>
    private var mainStateMaintainerFragment: StateManagerFragment? = null
    private var isRecreating: Boolean = false

    init {
        mainFragmentManager = WeakReference(fragmentManager)
    }

    /**
     * Creates the Fragment responsible to maintain the objects.
     * @return  true: fragment just created
     */
    fun firstTimeIn(): Boolean {
        try {
             // Recover reference
             mainStateMaintainerFragment = mainFragmentManager.get()!!.findFragmentByTag(mainStateMaintenerTag) as? StateManagerFragment

             //  Create new RetainedFragment
             if (mainStateMaintainerFragment == null) {
                 Log.d(TAG, "Create new RetainedFragment " + mainStateMaintenerTag)
                 mainStateMaintainerFragment = StateManagerFragment()
                 Log.d(TAG, "Created new  mainStateMaintainerFragment " +  mainStateMaintainerFragment.toString())
                 mainFragmentManager.get()!!.beginTransaction().add(mainStateMaintainerFragment, mainStateMaintenerTag).commit()
                 isRecreating = false
                 return true
             } else {
                 Log.d(TAG, "Return retained fragment  " + mainStateMaintenerTag);
                 isRecreating = true;
                 return false;
             }
        } catch (e: NullPointerException) {
            Log.w(TAG, "Error firstTimeIn()")
            return false
        }

    }

    /**
     * Return **true** it the current Activity was recreated at least one time
     * @return  If the Activity was recreated
     */
    fun wasRecreated(): Boolean {
        return isRecreating
    }


    /**
     *
     * @param key   object's TAG
     * @param any   object to maintain
     */
    fun put(key: String, any: Any) {
        mainStateMaintainerFragment!!.put(key, any)
    }

    /**
     * Insert the object to be preserved.
     * @param obj   object to maintain
     */
    fun put(obj: Any) {
        put(obj.javaClass.name, obj)
    }


    /**
     * Recovers the object saved
     * @param key   Object's TAG
     * @param <T>   Object type
     * @return      Object saved
    </T> */
    operator fun get(key: String): Any? {
        return mainStateMaintainerFragment!!.get(key)

    }

    /**
     * Checks the existence of a given object
     * @param key   Key to verification
     * @return      true: Object exists
     */
    fun hasKey(key: String): Boolean {
        return mainStateMaintainerFragment!!.get(key) != null
    }


    /**
     * Fragment resposible to preserve objects.
     * Instantiated only once. Uses a hashmap to save objs
     */
    class StateManagerFragment : Fragment() {
        private var mainData = HashMap<String, Any>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.d("In onCreate", savedInstanceState.toString())
            // Grants that the fragment will be preserved
            setRetainInstance(true)
        }

        /**
         * @param key   Reference key
         * @param any   Object to be saved
         */
        fun put(key: String, any: Any) {
            mainData.put(key, any)
        }

        /**
         * @param any    Object to be saved
         */
        fun put(any: Any) {
            put(any.javaClass.name, any)
        }

        /**
         * Recovers saved object
         * @param key   Reference key
         * @param <T>   Object type
         * @return      Object saved
        </T> */
        operator fun get(key: String): Any? {
            return mainData.get(key)
        }
    }

}