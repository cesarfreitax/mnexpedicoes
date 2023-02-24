package com.cesar.mnexpedicoes.fragments.schedule.presentation

import androidx.lifecycle.ViewModel
import com.cesar.mnexpedicoes.core.network.RetrofitInstance
import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragmentViewModel () : ViewModel() {

    val events = arrayListOf<EventResponse>()

    fun getSchedule(callback : () -> Unit) {
        RetrofitInstance.apiInterface.getSchedule()
            .enqueue( object : Callback<ArrayList<EventResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<EventResponse>>,
                    response: Response<ArrayList<EventResponse>>
                ) {
                    if (events.isEmpty()) {
                        response.body()?.map {
                            val event = eventMapper(it)
                            events.add(event)
                        }
                    }
                    callback()
                }

                override fun onFailure(call: Call<ArrayList<EventResponse>>, t: Throwable) {
                    throw(Exception(t.message))
                }

            })
    }

    fun getEventsFiltered(type: String) = events.filter { it.type == type }.toMutableList()

    private fun eventMapper(it: EventResponse) =
        EventResponse(
            id_event = it.id_event,
            type = it.type,
            title = it.title,
            date = it.date,
            endDate = it.endDate,
            hour = it.hour,
            img = it.img,
            status = it.status,
            description = it.description,
            tickets = it.tickets,
            locations = it.locations,
            includeds = it.includeds,
            notIncludeds = it.notIncludeds
        )
}