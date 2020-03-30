package black.bracken.picsorter.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import black.bracken.picsorter.repository.imageobserver.ImageObserverRepository
import black.bracken.picsorter.repository.settings.SettingsRepository

class TopViewModel(
    private val imageObserverRepository: ImageObserverRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val enablesObserver by lazy { MutableLiveData<Boolean>(imageObserverRepository.verifyWhetherAvailable()) }
    val runsOnBoot by lazy { MutableLiveData<Boolean>(settingsRepository.shouldRunOnBoot) }

    fun switchToEnableImageObserver(isEnabled: Boolean) {
        if (isEnabled) {
            imageObserverRepository.enableObserver()
        } else {
            imageObserverRepository.disableObserver()
        }
    }

    fun switchToRunOnBoot(isEnabled: Boolean) {
        settingsRepository.shouldRunOnBoot = isEnabled
    }

}