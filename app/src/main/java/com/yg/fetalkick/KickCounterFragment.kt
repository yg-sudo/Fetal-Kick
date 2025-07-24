package com.yg.fetalkick

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.time.LocalDate

// Assuming KickViewModel is defined in its own file (KickViewModel.kt)
// and KickViewModelFactory is also defined if needed.

class KickCounterFragment : Fragment() { // No arguments in primary constructor

    // Declare your ViewModel. Initialize it in onCreate or onViewCreated.
    private lateinit var viewModel: KickViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize your ViewModel here.
        // If KickViewModel requires an Application context (because it's an AndroidViewModel):
        val factory = KickViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[KickViewModel::class.java]

        // If KickViewModel is a simple ViewModel (doesn't need Application):
        // viewModel = ViewModelProvider(this)[KickViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_kick_counter, container, false)
        // viewModel is already initialized in onCreate

        // Calendar
        val dateButton = root.findViewById<Button>(R.id.dateButton)
        // Observe LiveData from the ViewModel
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateButton.text = date.toString()
        }
        dateButton.setOnClickListener {
            // Access selectedDate via the viewModel instance
            val date = viewModel.selectedDate.value ?: LocalDate.now()
            val dpd = DatePickerDialog(requireContext(), { _, y, m, d ->
                viewModel.selectDate(LocalDate.of(y, m + 1, d))
            }, date.year, date.monthValue - 1, date.dayOfMonth)
            dpd.datePicker.maxDate = System.currentTimeMillis() // Or use Calendar.getInstance().timeInMillis
            dpd.show()
        }

        // Meal sections - All references to viewModel.xxx should now correctly point
        // to your KickViewModel instance.
        setupMealSection(
            root,
            R.id.breakfastCount, R.id.breakfastMinus, R.id.breakfastPlus,
            R.id.breakfastStopwatch, R.id.breakfastTimer,
            viewModel.breakfastCount, // StateFlow from KickViewModel
            viewModel.breakfastTimerLD, // LiveData from KickViewModel
            viewModel.breakfastRunning, // StateFlow from KickViewModel
            { viewModel.incrementBreakfast() },
            { viewModel.decrementBreakfast() },
            { count -> viewModel.setBreakfastCount(count) }, // Pass the count
            { viewModel.toggleBreakfastTimer() }
        )
        setupMealSection(
            root,
            R.id.lunchCount, R.id.lunchMinus, R.id.lunchPlus,
            R.id.lunchStopwatch, R.id.lunchTimer,
            viewModel.lunchCount,
            viewModel.lunchTimerLD,
            viewModel.lunchRunning,
            { viewModel.incrementLunch() },
            { viewModel.decrementLunch() },
            { count -> viewModel.setLunchCount(count) }, // Pass the count
            { viewModel.toggleLunchTimer() }
        )
        setupMealSection(
            root,
            R.id.snacksCount, R.id.snacksMinus, R.id.snacksPlus,
            R.id.snacksStopwatch, R.id.snacksTimer,
            viewModel.snacksCount,
            viewModel.snacksTimerLD,
            viewModel.snacksRunning,
            { viewModel.incrementSnacks() },
            { viewModel.decrementSnacks() },
            { count -> viewModel.setSnacksCount(count) }, // Pass the count
            { viewModel.toggleSnacksTimer() }
        )
        setupMealSection(
            root,
            R.id.dinnerCount, R.id.dinnerMinus, R.id.dinnerPlus,
            R.id.dinnerStopwatch, R.id.dinnerTimer,
            viewModel.dinnerCount,
            viewModel.dinnerTimerLD,
            viewModel.dinnerRunning,
            { viewModel.incrementDinner() },
            { viewModel.decrementDinner() },
            { count -> viewModel.setDinnerCount(count) }, // Pass the count
            { viewModel.toggleDinnerTimer() }
        )
        return root
    }

    private fun setupMealSection(
        root: View,
        countId: Int, minusId: Int, plusId: Int,
        stopwatchId: Int, timerId: Int,
        countFlow: kotlinx.coroutines.flow.StateFlow<Int>,
        timerLiveData: androidx.lifecycle.LiveData<Int>,
        runningFlow: kotlinx.coroutines.flow.StateFlow<Boolean>,
        onPlus: () -> Unit,
        onMinus: () -> Unit,
        onSet: (Int) -> Unit,
        onStopwatch: () -> Unit
    ) {
        val countEdit = root.findViewById<EditText>(countId)
        val plusBtn = root.findViewById<Button>(plusId)
        val minusBtn = root.findViewById<Button>(minusId)
        val stopwatchBtn = root.findViewById<Button>(stopwatchId)
        val timerText = root.findViewById<TextView>(timerId)

        // Only allow positive integers
        countEdit.inputType = InputType.TYPE_CLASS_NUMBER
        countEdit.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val filtered = source.filter { it.isDigit() }
            if (filtered.isEmpty() && source.isNotEmpty()) "" else null // Allow empty string for clearing, otherwise only digits
        })

        // Collect countFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                countFlow.collect { count ->
                    if (countEdit.text.toString() != count.toString()) {
                        countEdit.setText(count.toString())
                        countEdit.setSelection(countEdit.text.length) // Optional: move cursor to end
                    }
                }
            }
        }
        countEdit.addTextChangedListener { editable ->
            val value = editable?.toString()?.toIntOrNull() ?: 0
            // To prevent feedback loop if collect is also setting text,
            // you might want to check if the value actually changed due to user input.
            // For now, this direct call to onSet might be fine if your ViewModel handles idempotency.
            if (countFlow.value != value) { // Only call onSet if the new value is different
                onSet(value)
            }
        }

        plusBtn.setOnClickListener { onPlus() }
        minusBtn.setOnClickListener { onMinus() }

        // Observe timerLiveData
        timerLiveData.observe(viewLifecycleOwner) { seconds ->
            timerText.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
        }

        // Collect runningFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                runningFlow.collect { running ->
                    stopwatchBtn.text = if (running) getString(R.string.stop_timer) else getString(R.string.start_stopwatch)
                }
            }
        }
        stopwatchBtn.setOnClickListener { onStopwatch() }
    }
}


