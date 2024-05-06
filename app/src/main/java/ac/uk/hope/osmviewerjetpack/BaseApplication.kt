package ac.uk.hope.osmviewerjetpack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// creating the base lifetime of our hilt dependencies

@HiltAndroidApp
class BaseApplication: Application()