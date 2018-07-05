package uk.ac.ncl.openlab.irismsg.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

import uk.ac.ncl.openlab.irismsg.OrganisationListActivity

@Suppress("unused")
@Module
abstract class OrganisationListActivityModule {
    
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): OrganisationListActivity
    
}