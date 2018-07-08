package uk.ac.ncl.openlab.irismsg.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uk.ac.ncl.openlab.irismsg.activity.EmptyMainActivity

@Module
@Suppress("unused")
abstract class EmptyMainActivityModule {
    
    @ContributesAndroidInjector(modules = [ FragmentBuildersModule::class ])
    abstract fun contributeMainActivity(): EmptyMainActivity
    
}