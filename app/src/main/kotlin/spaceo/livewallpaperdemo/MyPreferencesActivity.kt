package spaceo.livewallpaperdemo

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.widget.Toast

class MyPreferencesActivity : PreferenceActivity() {

    /**
     * Checks that a preference is a valid numerical value
     */
    internal var numberCheckListener: Preference.OnPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { preference, newValue ->
            // check that the string is an integer
            if (newValue != null && newValue.toString().length > 0
                && newValue.toString().matches("\\d*".toRegex())
            ) {
                return@OnPreferenceChangeListener true
            }
            // If now create a message to the user
            Toast.makeText(
                this@MyPreferencesActivity, resources.getString(R.string.lable_invalid_input),
                Toast.LENGTH_SHORT
            ).show()
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)

        // add a validator to the "numberofCircles" preference so that it only
        // accepts numbers
        val circlePreference = preferenceScreen.findPreference(resources.getString(R.string.lable_number_of_circles))

        // add the validator
        circlePreference.onPreferenceChangeListener = numberCheckListener
    }
}