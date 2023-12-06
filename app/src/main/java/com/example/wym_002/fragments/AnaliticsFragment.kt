package com.example.wym_002.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
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
import com.example.wym_002.db.MainDb
import com.example.wym_002.hidingPanel
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*


class AnaliticsFragment : Fragment() {

    private lateinit var binding: FragmentAnaliticsFragmentBinding

    private lateinit var dialog: Dialog
    private lateinit var dialogSetDate: DialogSetDateBinding
    private lateinit var dialogSetDateDatepicker: DialogSetDateDatepickerBinding

    lateinit var db: MainDb

    lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnaliticsFragmentBinding.inflate(layoutInflater)

        db = MainDb.getDb(this.activity!!)

        pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
        // resultCardBalance        key: R.drawable.credit_card_white.toString()
        // resultWalletBalance      key: R.drawable.wallet_white.toString()
        // resultBankBalance        key: R.drawable.account_balance_white.toString()
        //
        // progressBarMainProgress          key: mainProgress
        // progressBarMainMax               key: mainMax
        // progressBarMainColor               key: mainColor
        // progressBarSecondProgress        key: secondProgress
        // progressBarSecondMax             key: secondMax
        // progressBarSecondColor             key: secondColor
        // progressBarSavingProgress        key: savingProgress
        // progressBarSavingMax             key: savingMax
        // progressBarSavingColor             key: savingColor
        //
        // checkSplashScreen        key: getString(R.string.flagSplashScreen)      ОБРАТНЫЕ ПЕРЕМЕННЫЕ
        //                                                                        0 == TRUE   1 == FALSE
        // setDateDay               key: getString(R.string.setDateDay)
        //
        // deleteDateLast                   key: getString(R.string.deleteDateLast)

        buttonToEnterDate()
        buttonToSetList()

        val calendar = Calendar.getInstance()
        calcSetMonthDiagram(calendar)

        return binding.root
    }

    private fun setCurrentMonthBack(calendar: Calendar) {    // замена месяца на предыдущий

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) - 2))

        calcSetMonthDiagram(calendar)

    }

    private fun setCurrentMonthForward(calendar: Calendar) {  // замена месяца на следующий

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH)))

        calcSetMonthDiagram(calendar)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonsAndSwipeOnDisplayEnabled(calendar: Calendar){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewBack.visibility = View.VISIBLE
        binding.imageViewForward.visibility = View.VISIBLE

        binding.imageViewForward.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthForward(calendar)

        }

        binding.imageViewBack.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthBack(calendar)

        }

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setCurrentMonthForward(calendar)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setCurrentMonthBack(calendar)
            }
        })

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun buttonsAndSwipeOnDisplayDisabled(){

        binding.imageViewBack.visibility = View.GONE
        binding.imageViewForward.visibility = View.GONE

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {})

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

            calcSetMonthDiagram(Calendar.getInstance())

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

        buttonsAndSwipeOnDisplayDisabled()
        binding.textViewSave.visibility = View.GONE
        binding.textViewSaveSpend.visibility = View.GONE

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        var dateFromTemp = Calendar.getInstance()
        var dateToTemp = Calendar.getInstance()

        val calendar = Calendar.getInstance()
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
                if (month == -1) {
                    month = 11
                    year -= 1
                }
                dateFromTemp.set(Calendar.MONTH, month)
                dateFromTemp.set(Calendar.YEAR, year)
                dateFromTemp.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
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

            val dateFormatDiagram = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            calcDiagram(dateFormatDiagram.format(dateFromTemp.time),
                        dateFormatDiagram.format(dateToTemp.time),
                        dateFormatDiagram.format(dateFromTemp.time),
                        dateFormatDiagram.format(dateFromTemp.time))

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun calcSetYearDiagram() {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        buttonsAndSwipeOnDisplayDisabled()

        //TODO(НАПИСАТЬ ЧТОБЫ СЧИТАЛОСЬ ОТНОСИТЕЛЬНО ТЕКУЩЕГО ГОДА)
    }

    private fun calcSetMonthDiagram(calendar: Calendar) {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        binding.textViewMonth.text = when (calendar.get(Calendar.MONTH) + 1){
            1 -> "Январь"
            2 -> "Февраль"
            3 -> "Март"
            4 -> "Апрель"
            5 -> "Май"
            6 -> "Июнь"
            7 -> "Июль"
            8 -> "Август"
            9 -> "Сентябрь"
            10 -> "Октябрь"
            11 -> "Ноябрь"
            else -> "Декабрь"
        }

        binding.textViewYear.text = calendar.get(Calendar.YEAR).toString()

        buttonsAndSwipeOnDisplayEnabled(calendar)

        val month = calendar.get(Calendar.MONTH)
        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val resetDay = when (arrayOfMonths[month] <
                (pref.getInt(getString(R.string.setDateDay), 0) + 1)){
            true -> arrayOfMonths[month]
            else -> (pref.getInt(getString(R.string.setDateDay), 0) + 1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.DAY_OF_MONTH, resetDay)

        val dateFrom = dateFormat.format(calendar.time)          // дата "от"
        val dateSaving = calcDate(calendar)                 // дата для вывода сбережений


        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        when (month < 11){
            true -> {
                if (resetDay - 1 == 0){
                    calendar.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
                }
                else if (resetDay - 1 >= arrayOfMonths[month + 1]){
                    calendar.set(Calendar.DAY_OF_MONTH, (arrayOfMonths[month + 1] - 1))
                    calendar.set(Calendar.MONTH, (month + 1))
                }
                else {
                    calendar.set(Calendar.DAY_OF_MONTH, (resetDay - 1))
                    calendar.set(Calendar.MONTH, (month + 1))
                }
            }
            else -> {
                if (resetDay - 1 == 0){
                    calendar.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
                }
                else if (resetDay - 1 >= arrayOfMonths[0]){
                    calendar.set(Calendar.DAY_OF_MONTH, (arrayOfMonths[0] - 1))
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + 1))
                }
                else {
                    calendar.set(Calendar.DAY_OF_MONTH, (resetDay - 1))
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + 1))
                }
            }
        }
        val dateTo = dateFormat.format(calendar.time)           // дата "до"

        calcDiagram(dateFrom, dateTo, dateSaving, dateSaving)

    }

    private fun calcSetWeekDiagram() {

        binding.textViewSave.visibility = View.GONE
        binding.textViewSaveSpend.visibility = View.GONE

        buttonsAndSwipeOnDisplayDisabled()

        //TODO(НАПИСАТЬ ЧТОБЫ СЧИТАЛОСЬ ОТНОСИТЕЛЬНО ТЕКУЩЕЙ НЕДЕЛИ)
    }

    @SuppressLint("SetTextI18n")
    private fun showDataTimePickerWithMax(maxDate: Long, selectedDate: Calendar): Calendar{

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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
            this.activity!!, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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
    private fun calcDiagram(dateFrom: String, dateTo: String, dateSavingFrom: String, dateSavingTo: String) {

        //TODO(ДОПИСАТЬ ПОДСЧЕТ ОСТАЛЬНЫХ СЧЕТЧИКОВ НА ЭКРАНЕ)
        val threadTotalSpends = Thread {
            binding.textViewTotalSpend.text = db.getDao().getSumByDate(dateFrom, dateTo).toString()
        }
        threadTotalSpends.start()
        threadTotalSpends.join()

        val threadSaving = Thread {
            binding.textViewSaveSpend.text = db.getDao().getSavingByDate(dateSavingFrom, dateSavingTo).toString()
        }
        threadSaving.start()
        threadSaving.join()

        //TODO(СДЕЛАТЬ ПОДСЧЕТ И ВЫВОД В ДИАГРАММУ)

        binding.piechart.clearChart()       // чтобы не накладывал на предыдущее

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

    private fun calcDate(calendar: Calendar): String{

        // ЕСЛИ ДАТА БОЛЬШЕ ИЛИ РАВНА ДАТЕ НАЧАЛА УЧЕТА, ТО БЮДЖЕТ ДОБАВЛЯЕТСЯ В ПЕРВЫЙ ДЕНЬ ТЕКУЩЕГО МЕСЯЦА
        // ЕСЛИ ЖЕ ДАТА МЕНЬШЕ, ТО БЮДЖЕТ ДОБАВЛЯЕСЯ В ПЕРВОЕ ЧИСЛО ПРОШЛОГО МЕСЯЦА

        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        val setDateDayTemp = when (arrayOfMonths[calendar.get(Calendar.MONTH)] <
                (pref.getInt(getString(R.string.setDateDay), 0) + 1)){
            true -> arrayOfMonths[calendar.get(Calendar.MONTH)]
            else -> (pref.getInt(getString(R.string.setDateDay), 0) + 1)
        }

        when (setDateDayTemp <= calendar.get(Calendar.DAY_OF_MONTH)){
            true -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                return dateFormat.format(calendar.time)
            }
            else -> {
                var month = calendar.get(Calendar.MONTH) - 1
                var year = calendar.get(Calendar.YEAR)
                if (month == -1) {
                    month = 11
                    year -= 1
                }
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                return dateFormat.format(calendar.time)
            }
        }
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