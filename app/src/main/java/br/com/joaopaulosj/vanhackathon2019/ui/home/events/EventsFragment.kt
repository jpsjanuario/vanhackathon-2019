package br.com.joaopaulosj.vanhackathon2019.ui.home.events


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import br.com.joaopaulosj.vanhackathon2019.R
import br.com.joaopaulosj.vanhackathon2019.data.remote.models.EventResponse
import br.com.joaopaulosj.vanhackathon2019.utils.extensions.addTextWatcherDebounce
import br.com.joaopaulosj.vanhackathon2019.utils.extensions.notImplementedFeature
import br.com.joaopaulosj.vanhackathon2019.utils.extensions.setup
import kotlinx.android.synthetic.main.fragment_events.*
import org.jetbrains.anko.longToast
import java.util.*


class EventsFragment : Fragment(), EventsAdapter.OnItemClickListener, EventsContract.View {
	
	private val presenter: EventsContract.Presenter by lazy {
		val presenter = EventsPresenter()
		presenter.attachView(this)
		presenter
	}
	
	private val adapter by lazy {
		val adapter = EventsAdapter(activity!!, this)
		ViewCompat.setNestedScrollingEnabled(eventsRv, false)
		adapter.setHasStableIds(true)
		eventsRv.setup(adapter)
		adapter
	}
	
	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_events, container, false)
		return view
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		presenter.loadItems()
		setupSearch()
	}
	
	override fun onDestroy() {
		super.onDestroy()
		presenter.detachView()
	}
	
	private fun setupSearch() {
		eventsEt.addTextWatcherDebounce(200) {
			presenter.filter(eventsEt.text.toString()).apply {
				adapter.mList = this
			}
		}
	}
	
	override fun onItemClicked(item: EventResponse) {
		activity?.notImplementedFeature()
	}
	
	override fun onCalendarClicked(item: EventResponse) {
		val intent = Intent(Intent.ACTION_EDIT).apply {
			type = "vnd.android.cursor.item/event"
			putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, item.getStartCal().timeInMillis)
			putExtra(CalendarContract.EXTRA_EVENT_END_TIME, item.getEndCal().timeInMillis)
			putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
			putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getTimeZone("PST"))
			putExtra(CalendarContract.Events.TITLE, item.name)
			putExtra(CalendarContract.Events.DESCRIPTION, item.subtitle)
			putExtra(CalendarContract.Events.EVENT_LOCATION, "${item.city}, ${item.country}")
		}
		activity?.startActivity(intent)
	}
	
	override fun displayItems(items: List<EventResponse>) {
		adapter.mList = items
	}
	
	override fun displayError(msg: String?) {
		activity?.longToast(msg ?: "Error")
	}
	
	override fun displayLoading(loading: Boolean) {
	
	}
}
