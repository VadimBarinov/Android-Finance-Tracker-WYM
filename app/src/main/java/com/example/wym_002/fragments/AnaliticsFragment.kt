package com.example.wym_002.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import app.com.kotlinapp.OnSwipeTouchListener
import com.example.wym_002.R
import com.example.wym_002.databinding.DialogSetDateBinding
import com.example.wym_002.databinding.DialogSetDateDatepickerBinding
import com.example.wym_002.databinding.FragmentAnaliticsFragmentBinding
import com.example.wym_002.hidingPanel
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*


class AnaliticsFragment : Fragment() {

    private lateinit var binding: FragmentAnaliticsFragmentBinding

    private lateinit var dialog: Dialog
    private lateinit var dialogSetDate: DialogSetDateBinding
    private lateinit var dialogSetDateDatepicker: DialogSetDateDatepickerBinding

    private val calendar = Calendar.getInstance()

    private lateinit var animFlipIn: Animation
    private lateinit var animFlipOut: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnaliticsFragmentBinding.inflate(layoutInflater)

        animFlipIn = AnimationUtils.loadAnimation(this.activity!!, R.anim.flipin)
        animFlipOut = AnimationUtils.loadAnimation(this.activity!!, R.anim.flipout)

        buttonToEnterDate()
        buttonToSetList()


        return binding.root
    }

    override fun onStart() {      // указывает по умолчанию режим месяц
        super.onStart()

        calcSetMonthDiagram()
        calcDiagram()

    }

    private fun setCurrentMonthForward() {
        //TODO(НАПИСАТЬ ЗАМЕНУ МЕСЯЦА НА СЛЕДУЮЩИЙ)
    }

    private fun setCurrentMonthBack() {
        //TODO(НАПИСАТЬ ЗАМЕНУ МЕСЯЦА НА ПРЕДЫДУЩИЙ)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun swipeForwardBackEnabled() {

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setCurrentMonthForward()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                setCurrentMonthBack()
            }
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun swipeForwardBackDisabled() {

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
            }
        })

    }

    private fun buttonsOnDisplayEnabled(){

        buttonBack()
        buttonForward()

        binding.imageViewBack.visibility = View.VISIBLE
        binding.imageViewForward.visibility = View.VISIBLE

    }
    private fun buttonsOnDisplayDisabled(){

        binding.imageViewBack.visibility = View.GONE
        binding.imageViewForward.visibility = View.GONE

    }

    private fun buttonForward() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewForward.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthForward()

        }

    }

    private fun buttonBack() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewBack.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthBack()

        }

    }


    private fun buttonToEnterDate(){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutDate.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogEnterDate()

        }

    }

    private fun showDialogEnterDate() {

        dialogSetDate = DialogSetDateBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDate.root)
        dialog.setCancelable(true)

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogSetDate.textViewWeek.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetWeekDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewMonth.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetMonthDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewYear.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetYearDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewAnother.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            dialog.dismiss()

            calcSetAnotherDiagram()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun calcSetAnotherDiagram() {

        dialogSetDateDatepicker = DialogSetDateDatepickerBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDateDatepicker.root)
        dialog.setCancelable(true)

        binding.textViewSave.visibility = View.GONE
        binding.textViewSaveSpend.visibility = View.GONE

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        var dateFromTemp = Calendar.getInstance()
        var dateToTemp = Calendar.getInstance()

        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        when (dateFromTemp.get(Calendar.DAY_OF_MONTH) != 1){
            true -> {
                dateFromTemp.set(Calendar.DAY_OF_MONTH, dateToTemp.get(Calendar.DAY_OF_MONTH) - 1)
            }
            else -> {
                var month = dateFromTemp.get(Calendar.MONTH) - 1
                var year = dateFromTemp.get(Calendar.YEAR)
                if (month == 0) {
                    month = 12
                    year -= 1
                }
                dateFromTemp.set(Calendar.MONTH, month)
                dateFromTemp.set(Calendar.YEAR, year)
                dateFromTemp.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month - 1])
            }
        }

        dialogSetDateDatepicker.enterDataFrom.text = "От: ${dateFormat.format(dateFromTemp.time)}"
        dialogSetDateDatepicker.enterDataTo.text = "До: ${dateFormat.format(dateToTemp.time)}"

        dialogSetDateDatepicker.enterDataFrom.setOnClickListener{

            dateFromTemp = showDataTimePickerWithMax(dateToTemp.timeInMillis, dateFromTemp)

        }

        dialogSetDateDatepicker.enterDataTo.setOnClickListener{

            dateToTemp = showDataTimePickerWithMin(dateFromTemp.timeInMillis, dateToTemp)

        }

        dialogSetDateDatepicker.dialogBtn.setOnClickListener{

            binding.textViewMonth.text = "От: ${dateFormat.format(dateFromTemp.time)}"
            binding.textViewYear.text = "До: ${dateFormat.format(dateToTemp.time)}"

            buttonsOnDisplayDisabled()
            swipeForwardBackDisabled()

            //TODO(ФИЛЬТРОВАТЬ ПО ВЫБРАННЫМ ДАТАМ)

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun calcSetYearDiagram() {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        buttonsOnDisplayDisabled()
        swipeForwardBackDisabled()

        //TODO(НАПИСАТЬ ЧТОБЫ СЧИТАЛОСЬ ОТНОСИТЕЛЬНО ТЕКУЩЕГО ГОДА)
    }

    private fun calcSetMonthDiagram() {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        buttonsOnDisplayEnabled()
        swipeForwardBackEnabled()

        //TODO(НАПИСАТЬ ЧТОБЫ СЧИТАЛОСЬ ОТНОСИТЕЛЬНО ТЕКУЩЕГО МЕСЯЦА)
    }

    private fun calcSetWeekDiagram() {

        binding.textViewSave.visibility = View.GONE
        binding.textViewSaveSpend.visibility = View.GONE

        buttonsOnDisplayDisabled()
        swipeForwardBackDisabled()

        //TODO(НАПИСАТЬ ЧТОБЫ СЧИТАЛОСЬ ОТНОСИТЕЛЬНО ТЕКУЩЕЙ НЕДЕЛИ)
    }

    @SuppressLint("SetTextI18n")
    private fun showDataTimePickerWithMax(maxDate: Long, selectedDate: Calendar): Calendar{

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                selectedDate.set(Calendar.SECOND, 0)

                dialogSetDateDatepicker.enterDataFrom.text = "От: ${dateFormat.format(selectedDate.time)}"

                binding.textViewMonth.text = "От: ${dateFormat.format(selectedDate.time)}"

            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        hidingPanel(datePickerDialog)
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.show()

        return selectedDate

    }

    @SuppressLint("SetTextI18n")
    private fun showDataTimePickerWithMin(minDate: Long, selectedDate: Calendar): Calendar{

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                selectedDate.set(Calendar.SECOND, 0)

                dialogSetDateDatepicker.enterDataTo.text = "До: ${dateFormat.format(selectedDate.time)}"

                binding.textViewYear.text = "До: ${dateFormat.format(selectedDate.time)}"

            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        hidingPanel(datePickerDialog)
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()

        return selectedDate

    }


    @SuppressLint("ResourceType")
    private fun calcDiagram() {

        //TODO(СДЕЛАТЬ ПОДСЧЕТ И ВЫВОД В ДИАГРАММУ)

        binding.piechart.addPieSlice(
            PieModel(
                "house", 3F,
                Color.parseColor(getString(R.color.house))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "bus", 4F,
                Color.parseColor(getString(R.color.bus))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "foodHouse", 5.5F,
                Color.parseColor(getString(R.color.food_house))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "health", 8F,
                Color.parseColor(getString(R.color.health))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "coffee", 20F,
                Color.parseColor(getString(R.color.coffee))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "games", 8F,
                Color.parseColor(getString(R.color.games))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "clothes", 20F,
                Color.parseColor(getString(R.color.clothes))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "another", 31.5F,
                Color.parseColor(getString(R.color.another))
            )
        )

        binding.piechart.animationTime = 600

        binding.piechart.startAnimation()
    }


    private fun buttonToSetList() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutSetList.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            //TODO(ДОПИСАТЬ ПЕРЕХОД НА ДРУГОЙ ФРАГМЕНТ)

        }

    }


}