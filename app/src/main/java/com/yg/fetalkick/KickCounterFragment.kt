package com.yg.fetalkick

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.util.*

class KickCounterFragment : Fragment() {
    private lateinit var viewModel: KickViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_kick_counter, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[KickViewModel::class.java]

        // Calendar
        val dateButton = root.findViewById<Button>(R.id.dateButton)
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateButton.text = date.toString()
        }
        dateButton.setOnClickListener {
            val date = viewModel.selectedDate.value ?: LocalDate.now()
            val dpd = DatePickerDialog(requireContext(), { _, y, m, d ->
                viewModel.selectDate(LocalDate.of(y, m + 1, d))
            }, date.year, date.monthValue - 1, date.dayOfMonth)
            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }

        // Meal sections
        setupMealSection(
            root,
            R.id.breakfastLabel, R.id.breakfastCount, R.id.breakfastMinus, R.id.breakfastPlus,
            R.id.breakfastStopwatch, R.id.breakfastTimer, viewModel.breakfastCount, viewModel.breakfastTimer, viewModel.breakfastRunning,
            { viewModel.incrementBreakfast() }, { viewModel.decrementBreakfast() }, { viewModel.setBreakfastCount(it) }, { viewModel.toggleBreakfastTimer() }
        )
        setupMealSection(
            root,
            R.id.lunchLabel, R.id.lunchCount, R.id.lunchMinus, R.id.lunchPlus,
            R.id.lunchStopwatch, R.id.lunchTimer, viewModel.lunchCount, viewModel.lunchTimer, viewModel.lunchRunning,
            { viewModel.incrementLunch() }, { viewModel.decrementLunch() }, { viewModel.setLunchCount(it) }, { viewModel.toggleLunchTimer() }
        )
        setupMealSection(
            root,
            R.id.snacksLabel, R.id.snacksCount, R.id.snacksMinus, R.id.snacksPlus,
            R.id.snacksStopwatch, R.id.snacksTimer, viewModel.snacksCount, viewModel.snacksTimer, viewModel.snacksRunning,
            { viewModel.incrementSnacks() }, { viewModel.decrementSnacks() }, { viewModel.setSnacksCount(it) }, { viewModel.toggleSnacksTimer() }
        )
        setupMealSection(
            root,
            R.id.dinnerLabel, R.id.dinnerCount, R.id.dinnerMinus, R.id.dinnerPlus,
            R.id.dinnerStopwatch, R.id.dinnerTimer, viewModel.dinnerCount, viewModel.dinnerTimer, viewModel.dinnerRunning,
            { viewModel.incrementDinner() }, { viewModel.decrementDinner() }, { viewModel.setDinnerCount(it) }, { viewModel.toggleDinnerTimer() }
        )
        return root
    }

    private fun setupMealSection(
        root: View,
        labelId: Int, countId: Int, minusId: Int, plusId: Int,
        stopwatchId: Int, timerId: Int,
        countLive: androidx.lifecycle.LiveData<Int>, timerLive: androidx.lifecycle.LiveData<Int>, runningLive: androidx.lifecycle.LiveData<Boolean>,
        onPlus: () -> Unit, onMinus: () -> Unit, onSet: (Int) -> Unit, onStopwatch: () -> Unit
    ) {
        val countEdit = root.findViewById<EditText>(countId)
        val plusBtn = root.findViewById<Button>(plusId)
        val minusBtn = root.findViewById<Button>(minusId)
        val stopwatchBtn = root.findViewById<Button>(stopwatchId)
        val timerText = root.findViewById<TextView>(timerId)

        // Only allow positive integers
        countEdit.inputType = InputType.TYPE_CLASS_NUMBER
        countEdit.filters = arrayOf(InputFilter { source, _, _, dest, _, _ ->
            val filtered = source.filter { it.isDigit() }
            if (filtered.length != source.length) "" else null
        })
        countLive.observe(viewLifecycleOwner) { count ->
            if (countEdit.text.toString() != count.toString())
                countEdit.setText(count.toString())
        }
        countEdit.addTextChangedListener {
            val value = it?.toString()?.toIntOrNull() ?: 0
            onSet(value)
        }
        plusBtn.setOnClickListener { onPlus() }
        minusBtn.setOnClickListener { onMinus() }
        timerLive.observe(viewLifecycleOwner) { seconds ->
            timerText.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
        }
        runningLive.observe(viewLifecycleOwner) { running ->
            stopwatchBtn.text = if (running) "Stop Stopwatch" else "Start Stopwatch"
        }
        stopwatchBtn.setOnClickListener { onStopwatch() }
    }
}