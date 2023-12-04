package com.example.wym_002.fragments


import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.wym_002.R
import com.example.wym_002.databinding.CustomCashDialogLayoutBinding
import com.example.wym_002.databinding.CustomDialogLayoutBinding
import com.example.wym_002.databinding.FragmentMainFragmentBinding
import com.example.wym_002.db.Items
import com.example.wym_002.db.MainDb
import com.example.wym_002.db.Spends
import com.example.wym_002.hidingPanel
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainFragmentBinding
    private lateinit var dialog: Dialog
    private lateinit var customCashDialog: CustomCashDialogLayoutBinding
    private lateinit var customDialog: CustomDialogLayoutBinding
    lateinit var pref: SharedPreferences

    private var toast: Toast? = null

    lateinit var db: MainDb

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainFragmentBinding.inflate(layoutInflater)

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

        attachViewDragListenerAllIems()

        draggingElements()

        checkDateDeletePref()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        updatingVariables()         // чтобы каждый раз не считалось
    }

    private fun checkDateDeletePref(){

        val newDate = calcDate(Calendar.getInstance())
        val lastDate = pref.getString(getString(R.string.deleteDateLast), "")

        if (newDate != lastDate){

            saveData(R.drawable.credit_card_white.toString(), 0)
            saveData(R.drawable.wallet_white.toString(), 0)
            saveData(R.drawable.account_balance_white.toString(), 0)

            saveData("mainProgress", 0)
            saveData("mainMax", 0)
            saveData("mainColor", R.drawable.custom_progress_bar)

            saveData("secondProgress", 0)
            saveData("secondMax", 0)
            saveData("secondColor", R.drawable.custom_progress_bar)

            saveData("savingProgress", 0)
            saveData("savingMax", 0)
            saveData("savingColor", R.drawable.custom_progress_bar2)

            saveData(getString(R.string.deleteDateLast), newDate)

        }

    }


    private fun attachViewDragListenerAllIems() {
        // В ВИДЕ КЛЮЧА ПЕРЕДАЕТ ИКОНКУ В БЕЛОМ ЦВЕТЕ
        attachViewDragListener(binding.btnCard,
            R.drawable.credit_card,
            R.drawable.credit_card_white
        )
        attachViewDragListener(binding.btnWallet,
            R.drawable.wallet,
            R.drawable.wallet_white)
        attachViewDragListener(binding.btnBank,
            R.drawable.account_balance,
            R.drawable.account_balance_white
        )
    }


    private fun draggingElements() {
        startFunc(binding.house, R.drawable.house_white)
        startFunc(binding.bus, R.drawable.bus_white)
        startFunc(binding.foodHouse, R.drawable.food_house_white)
        startFunc(binding.health, R.drawable.health_white)
        startFunc(binding.coffee, R.drawable.coffee_white)
        startFunc(binding.games, R.drawable.games_white)
        startFunc(binding.clothes, R.drawable.clothes_white)
        startFunc(binding.another, R.drawable.another_white)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updatingVariables() {       // обновляет счетчики на экране

        val resultCardBalance = pref.getInt(R.drawable.credit_card_white.toString(), 0)
        val resultWalletBalance = pref.getInt(R.drawable.wallet_white.toString(), 0)
        val resultBankBalance = pref.getInt(R.drawable.account_balance_white.toString(), 0)

        binding.cardBalance.text = resultCardBalance.toString()
        binding.walletBalance.text = resultWalletBalance.toString()
        binding.bankBalance.text = resultBankBalance.toString()



        // progressBarMainProgress          key: mainProgress
        // progressBarMainMax               key: mainMax
        // progressBarSecondProgress        key: secondProgress
        // progressBarSecondMax             key: secondMax
        // progressBarSavingProgress        key: savingProgress
        // progressBarSavingMax             key: savingMax

        val animMain = ProgressBarAnimation(binding.progressBarMain,
            binding.progressBarMain.progress.toFloat(), pref.getInt("mainProgress", 0).toFloat()
        )
        animMain.duration = 500

        val animSecond = ProgressBarAnimation(binding.progressBarSecondary,
            binding.progressBarSecondary.progress.toFloat(), pref.getInt("secondProgress", 0).toFloat()
        )
        animSecond.duration = 500

        val animSaving = ProgressBarAnimation(binding.progressBarSaving,
            binding.progressBarSaving.progress.toFloat(), pref.getInt("savingProgress", 0).toFloat()
        )
        animSaving.duration = 500

        binding.mainPr.text = pref.getInt("mainMax", 0).toString()
        binding.mainPr2.text = pref.getInt("mainProgress", 0).toString()
        binding.progressBarMain.max = pref.getInt("mainMax", 0)
        binding.progressBarMain.startAnimation(animMain)
        binding.progressBarMain.progress = pref.getInt("mainProgress", 0)

        binding.secondaryPr.text = pref.getInt("secondMax", 0).toString()
        binding.secondaryPr2.text = pref.getInt("secondProgress", 0).toString()
        binding.progressBarSecondary.max = pref.getInt("secondMax", 0)
        binding.progressBarSecondary.startAnimation(animSecond)
        binding.progressBarSecondary.progress = pref.getInt("secondProgress", 0)

        binding.spendPr.text = pref.getInt("savingMax", 0).toString()
        binding.spendPr2.text = pref.getInt("savingProgress", 0).toString()
        binding.progressBarSaving.max = pref.getInt("savingMax", 0)
        binding.progressBarSaving.startAnimation(animSaving)
        binding.progressBarSaving.progress = pref.getInt("savingProgress", 0)

        binding.progressBarMain.progressDrawable = resources.getDrawable(
            pref.getInt("mainColor", R.drawable.custom_progress_bar))
        binding.progressBarSecondary.progressDrawable = resources.getDrawable(
            pref.getInt("secondColor", R.drawable.custom_progress_bar))
        binding.progressBarSaving.progressDrawable = resources.getDrawable(
            pref.getInt("savingColor", R.drawable.custom_progress_bar2))

    }


    private fun showToastMsg(string: String) {

        val container = this.activity!!.findViewById<ViewGroup>(R.id.custom_toast_container)
        val layout = layoutInflater.inflate(R.layout.custom_toast, container)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = string

        if (toast != null){
            toast!!.cancel()
        }
        toast = Toast(this.activity!!.applicationContext)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.view = layout
        toast!!.show()

    }

    private fun showToastMsgRed(string: String) {

        val container = this.activity!!.findViewById<ViewGroup>(R.id.custom_toast_container)
        val layout = layoutInflater.inflate(R.layout.custom_toast_red, container)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = string

        if (toast != null){
            toast!!.cancel()
        }
        toast = Toast(this.activity!!.applicationContext)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()

    }

    private fun startFunc(image: ImageView, drawableIcon: Int){

        fun showCustomDialog(balance: TextView, drawableIconKey: Int) {

            customDialog = CustomDialogLayoutBinding.inflate(layoutInflater)

            customDialog.iconOnDialog.setImageResource(drawableIcon)
            customDialog.textShowCat.text = when (drawableIcon){

                R.drawable.house_white -> { getString(R.string.house) }
                R.drawable.bus_white -> { getString(R.string.bus) }
                R.drawable.food_house_white -> { getString(R.string.food_house) }
                R.drawable.health_white -> { getString(R.string.health) }
                R.drawable.coffee_white -> { getString(R.string.coffee) }
                R.drawable.games_white -> { getString(R.string.games) }
                R.drawable.clothes_white -> { getString(R.string.clothes) }
                else -> { getString(R.string.another) }

            }

            dialog = Dialog(this.activity!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(customDialog.root)
            dialog.setCancelable(true)

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.SECOND, 0)

            // указывается дата по умолчанию
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val textDate = "Дата: ${dateFormat.format(calendar.time)}"
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val textTime = "Время: ${timeFormat.format(calendar.time)}"
            customDialog.enterData.text = "$textDate        $textTime"

            val dateFormatForReturn = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            var dataTimeSelected = calendar

            customDialog.enterData.setOnClickListener {
                dataTimeSelected = showDataTimePicker()
            }

            customDialog.dialogBtn.setOnClickListener {

                val res = customDialog.enterSum.text.toString()   // сумма траты
                val spendCat = customDialog.enterCat.text.toString() // категория траты

                val keyCatForPref = when (drawableIcon){     // main or second

                    R.drawable.house_white-> { "main" }
                    R.drawable.bus_white -> { "main" }
                    R.drawable.food_house_white-> { "main" }
                    R.drawable.health_white -> { "main" }
                    else -> { "second" }

                }

                if (res.isNotEmpty()) {
                    if(Integer.parseInt(res) != 0){
                        if (Integer.parseInt(balance.text.toString()) >= Integer.parseInt(res)) {
                            if (spendCat.isNotEmpty()) {

                                val dataTimeSelectedString = dateFormatForReturn.format(dataTimeSelected.time)

                                // проверка на добавление траты за другой месяц
                                if (calcDate(dataTimeSelected) == calcDate(Calendar.getInstance())) {
                                    val dataToSave = Integer.parseInt(balance.text.toString()) - Integer.parseInt(res)
                                    // пересчитывает баланс на картах
                                    saveData(drawableIconKey.toString(), dataToSave)

                                    calcProgressBar(keyCatForPref, Integer.parseInt(res))
                                }

                                val cardKeyForDb = when (drawableIconKey) {
                                    R.drawable.credit_card_white -> {
                                        "card"
                                    }
                                    R.drawable.wallet_white -> {
                                        "wallet"
                                    }
                                    else -> {
                                        "account"
                                    }
                                }

                                val item = Items(     // создает элемент для добавления
                                    null,
                                    dataTimeSelectedString,
                                    spendCat,
                                    customDialog.textShowCat.text.toString(),
                                    cardKeyForDb,
                                    Integer.parseInt(res)
                                )

                                // ДОБАВЛЯЕТ ЗАПСИСЬ В БД
                                Thread { db.getDao().insertItem(item) }.start()

                                // ОБНОВЛЯЕТ ДАННЫЕ О СБЕРЕЖЕНИЯХ В БД
                                Thread{

                                    val textDateForUpdate = calcDate(Calendar.getInstance())    // формируется ключ

                                    val pastTotal = when (db.getDao().getBudgetData(textDateForUpdate)) {
                                        null -> 0
                                        else -> db.getDao().getBudgetData(textDateForUpdate)
                                    }

                                    val resultTotalBalance = pref.getInt(R.drawable.credit_card_white.toString(), 0) +
                                            pref.getInt(R.drawable.wallet_white.toString(), 0) +
                                            pref.getInt(R.drawable.account_balance_white.toString(), 0)

                                    val spend = Spends(
                                        textDateForUpdate,
                                        resultTotalBalance,
                                        pastTotal!!
                                    )
                                    Thread{
                                        db.getDao().insertSpend(spend)
                                    }.start()
                                }.start()

                                dialog.dismiss()

                            } else showToastMsg(getString(R.string.plsEnterSpend))
                        }else showToastMsg(getString(R.string.plsNoMoney))
                    }else showToastMsg(getString(R.string.plsEnterSumCorrect))
                }else showToastMsg(getString(R.string.plsEnterSum))

                updatingVariables()
            }

            hidingPanel(dialog)
            dialog.show()

        }

        // проверяет и вызывает окно для добавления траты
        fun checkIfItemOnCat(dragEvent: DragEvent, draggableItem: View) {

            val catXStart = binding.house.x
            val catYStart = binding.house.y

            val catXEnd = catXStart + binding.house.width
            val catYEnd = catYStart + binding.house.height

            if (dragEvent.x in catXStart..catXEnd && dragEvent.y in catYStart..catYEnd){
                // проверка на то, откуда потратил
                when (draggableItem) {
                    binding.btnCard -> showCustomDialog(binding.cardBalance,
                        R.drawable.credit_card_white
                    )
                    binding.btnWallet -> showCustomDialog(binding.walletBalance,
                        R.drawable.wallet_white
                    )
                    binding.btnBank -> showCustomDialog(binding.bankBalance,
                        R.drawable.account_balance_white
                    )
                }
            }

        }

        val maskDragListener = View.OnDragListener { view, dragEvent ->

            val draggableItem = dragEvent.localState as View

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    image.alpha = 0.3f          // затемняет картинку еды
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // возвращает в начальное положение категорию, если иконка не в нужной зоне
                    image.alpha = 1.0f
                    draggableItem.visibility = View.INVISIBLE
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // возвращает категорию, если иконку отпустили
                    image.alpha = 1.0f

                    //возвращает иконку на место
                    if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        val draggedData = dragEvent.clipData.getItemAt(0).text
                        println("draggedData $draggedData")
                    }
                    checkIfItemOnCat(dragEvent, draggableItem)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    draggableItem.visibility = View.VISIBLE
                    view.invalidate()
                    true
                }
                else -> {
                    false
                }

            }
        }

        image.setOnDragListener(maskDragListener)

    }

    private fun showDataTimePicker(): Calendar{

        val selectedDate = Calendar.getInstance()
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                val timePickerDialog = TimePickerDialog(
                    this.activity!!, { TimePicker, hourOfDay: Int, minute: Int ->
                        selectedDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        val textDate = "Дата: ${dateFormat.format(selectedDate.time)}"
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val textTime = "Время: ${timeFormat.format(selectedDate.time)}"
                        selectedDate.set(Calendar.SECOND, 0)
                        customDialog.enterData.text = "$textDate        $textTime"
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )

                hidingPanel(timePickerDialog)
                timePickerDialog.show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        hidingPanel(datePickerDialog)
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()

        return selectedDate

    }

    @SuppressLint("ClickableViewAccessibility")      // слушатель движений
    private fun attachViewDragListener(imageButton: ImageButton, drawable: Int, drawableIcon: Int) {

        // параметры анимаций принажатии и отпускании
        val buttonClick1 = AlphaAnimation(1f, 0f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.5f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        // считыватель касаний
        imageButton.setOnTouchListener { view: View, event: MotionEvent ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                view.startAnimation(buttonClick1)

                view.tag = true
            }

            // проверка на зажатие
            if (view.isPressed && view.tag as Boolean && event.action == MotionEvent.ACTION_MOVE) {

                val eventDuration = event.eventTime - event.downTime + 250
                if (eventDuration >= ViewConfiguration.getLongPressTimeout()) {

                    view.tag = true

                    val item = ClipData.Item(R.string.dragMessage.toString())

                    val dataToDrag = ClipData(
                        R.string.dragMessage.toString(),
                        arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                        item
                    )

                    val maskShadow = DragShadowBuilder(view, drawable)

                    // если зажата, то двигается
                    view.startDragAndDrop(dataToDrag, maskShadow, view, 0)

                    view.visibility = View.INVISIBLE
                    true
                }
                // иначе срабатывает слушатель нажатий
                else {
                    imageButton.setOnClickListener {

                        showCustomCashDialog(drawableIcon)
                        view.startAnimation(buttonClick2)
                        it.visibility = View.VISIBLE
                    }
                    true
                }


            }
            else {
                false
            }


        }
    }


    private fun showCustomCashDialog(drawableIcon: Int) {

        customCashDialog = CustomCashDialogLayoutBinding.inflate(layoutInflater)
        customCashDialog.iconOnDialog.setImageResource(drawableIcon)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(customCashDialog.root)
        dialog.setCancelable(true)

        customCashDialog.dialogBtn.setOnClickListener {

            val res: String = customCashDialog.enterSum.text.toString()
            if (res.isNotEmpty()) {

                if (Integer.parseInt(res) != 0) {

                    val textDate = calcDate(Calendar.getInstance())    // формируется ключ

                    val past = pref.getInt(drawableIcon.toString(), 0)

                    saveData(drawableIcon.toString(), (past + Integer.parseInt(res)))

                    calcProgressBarPlus(Integer.parseInt(res))

                    Thread{
                        val pastTotal = when (db.getDao().getBudgetData(textDate)) {
                            null -> 0
                            else -> db.getDao().getBudgetData(textDate)
                        }

                        val resultTotalBalance = pref.getInt(R.drawable.credit_card_white.toString(), 0) +
                                pref.getInt(R.drawable.wallet_white.toString(), 0) +
                                pref.getInt(R.drawable.account_balance_white.toString(), 0)

                        val spend = Spends(
                            textDate,
                            resultTotalBalance,
                            pastTotal!! + Integer.parseInt(res)
                        )
                        Thread{
                            db.getDao().insertSpend(spend)
                        }.start()
                    }.start()

                    dialog.dismiss()

                }else showToastMsg(getString(R.string.plsEnterSumCorrect))

            }else showToastMsg(getString(R.string.plsEnterSum))

            updatingVariables()
        }

        hidingPanel(dialog)
        dialog.show()

    }


    @SuppressLint("CommitPrefEdits")
    private fun saveData(key: String, dataToSave: Int) {

        val editor = pref.edit()
        editor?.putInt(key, dataToSave)
        editor?.apply()

    }

    @SuppressLint("CommitPrefEdits")
    private fun saveData(key: String, dataToSave: String) {

        val editor = pref.edit()
        editor?.putString(key, dataToSave)
        editor?.apply()

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

        val setDateDayTemp = when (arrayOfMonths[calendar.get(Calendar.MONTH) - 1] <
                (pref.getInt(getString(R.string.setDateDay), 0) + 1)){
            true -> arrayOfMonths[calendar.get(Calendar.MONTH) - 1]
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
                if (month == 0) {
                    month = 12
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


    // сохраняет данные о прогресс барах в pref
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun calcProgressBar(key: String, res: Int){

        // progressBarMainProgress          key: mainProgress
        // progressBarMainMax               key: mainMax
        // progressBarMainColor               key: mainColor
        // progressBarSecondProgress        key: secondProgress
        // progressBarSecondMax             key: secondMax
        // progressBarSecondColor             key: secondColor
        // progressBarSavingProgress        key: savingProgress
        // progressBarSavingMax             key: savingMax
        // progressBarSavingColor             key: savingColor


        when (key) {
            "main" -> {
                val resForSave = pref.getInt("mainProgress", 0) + res
                val resMinus = pref.getInt("mainMax", 0) - resForSave

                if (resMinus > 0){
                    saveData("mainProgress", resForSave)
                }
                else if (resMinus == 0){
                    saveData("mainProgress", resForSave)
                    saveData("mainColor", R.drawable.custom_progress_bar3)
                    binding.progressBarMain.progressDrawable = resources.getDrawable(
                        pref.getInt("mainColor", R.drawable.custom_progress_bar))

                }
                else{

                    showToastMsgRed(getString(R.string.limitError))

                    saveData("mainProgress", resForSave)
                    saveData("mainMax", resForSave)

                    saveData("mainColor", R.drawable.custom_progress_bar3)
                    binding.progressBarMain.progressDrawable = resources.getDrawable(
                        pref.getInt("mainColor", R.drawable.custom_progress_bar))

                    val resMinusSaving = pref.getInt("savingProgress", 0) + resMinus

                    if (resMinusSaving < 0 ){

                        saveData("savingProgress", 0)
                        saveData("secondMax", pref.getInt("secondMax", 0) + resMinusSaving)

                        saveData("savingColor", R.drawable.custom_progress_bar3)
                        binding.progressBarSaving.progressDrawable = resources.getDrawable(
                            pref.getInt("savingColor", R.drawable.custom_progress_bar2))

                        if (pref.getInt("secondMax", 0) == pref.getInt("secondProgress", 0)){

                            saveData("secondColor", R.drawable.custom_progress_bar3)
                            binding.progressBarSecondary.progressDrawable = resources.getDrawable(
                                pref.getInt("secondColor", R.drawable.custom_progress_bar2))

                        }

                        if (pref.getInt("secondMax", 0) <= 0){

                            saveData("secondMax", 0)
                            saveData("secondProgress", 0)

                            saveData("secondColor", R.drawable.custom_progress_bar3)
                            binding.progressBarSecondary.progressDrawable = resources.getDrawable(
                                pref.getInt("secondColor", R.drawable.custom_progress_bar2))

                        }

                    }
                    else {

                        saveData("savingProgress", resMinusSaving)
                        saveData("savingColor", R.drawable.custom_progress_bar3)
                        binding.progressBarSaving.progressDrawable = resources.getDrawable(
                            pref.getInt("savingColor", R.drawable.custom_progress_bar2))

                    }
                }

            }
            // "second"
            else -> {

                val resForSave = pref.getInt("secondProgress", 0) + res
                val resMinus = pref.getInt("secondMax", 0) - resForSave

                if (resMinus > 0){
                    saveData("secondProgress", resForSave)
                }
                else if (resMinus == 0){
                    saveData("secondProgress", resForSave)
                    saveData("secondColor", R.drawable.custom_progress_bar3)
                    binding.progressBarSecondary.progressDrawable = resources.getDrawable(
                        pref.getInt("secondColor", R.drawable.custom_progress_bar))
                }
                else{

                    showToastMsgRed(getString(R.string.limitError))

                    saveData("secondProgress", resForSave)
                    saveData("secondMax", resForSave)

                    saveData("secondColor", R.drawable.custom_progress_bar3)
                    binding.progressBarSecondary.progressDrawable = resources.getDrawable(
                        pref.getInt("secondColor", R.drawable.custom_progress_bar))

                    val resMinusSaving = pref.getInt("savingProgress", 0) + resMinus

                    if (resMinusSaving < 0 ){

                        saveData("savingProgress", 0)
                        saveData("mainMax", pref.getInt("mainMax", 0) + resMinusSaving)

                        saveData("savingColor", R.drawable.custom_progress_bar3)
                        binding.progressBarSaving.progressDrawable = resources.getDrawable(
                            pref.getInt("savingColor", R.drawable.custom_progress_bar2))

                        if (pref.getInt("mainMax", 0) == pref.getInt("mainProgress", 0)){

                            saveData("mainColor", R.drawable.custom_progress_bar3)
                            binding.progressBarMain.progressDrawable = resources.getDrawable(
                                pref.getInt("mainColor", R.drawable.custom_progress_bar2))

                        }

                        if (pref.getInt("mainMax", 0) <= 0){

                            saveData("mainMax", 0)
                            saveData("mainProgress", 0)

                            saveData("mainColor", R.drawable.custom_progress_bar3)
                            binding.progressBarMain.progressDrawable = resources.getDrawable(
                                pref.getInt("mainColor", R.drawable.custom_progress_bar2))

                        }

                    }
                    else {

                        saveData("savingProgress", resMinusSaving)
                        saveData("savingColor", R.drawable.custom_progress_bar3)
                        binding.progressBarSaving.progressDrawable = resources.getDrawable(
                            pref.getInt("savingColor", R.drawable.custom_progress_bar2))

                    }
                }

            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun calcProgressBarPlus(res: Int){
        val textDate = calcDate(Calendar.getInstance())    // формируется ключ
        val thread = Thread{
            val resTotal = when (db.getDao().getBudgetData(textDate)) {
                null -> 0
                else -> db.getDao().getBudgetData(textDate)
            }
            if (resTotal == 0){
                saveData("mainMax", (res * 0.5).toInt())
                saveData("secondMax", (res * 0.3).toInt())
                saveData("savingMax", (res * 0.2).toInt())
                saveData("savingProgress", (res * 0.2).toInt())
            }
            else {
                val mainMax = pref.getInt("mainMax", 0)
                val secondMax = pref.getInt("secondMax", 0)
                val savingMax = pref.getInt("savingMax", 0)
                val savingProgress = pref.getInt("savingProgress", 0)

                saveData("mainMax", (mainMax + (res * 0.5).toInt()))
                saveData("secondMax",(secondMax + (res * 0.3).toInt()))
                saveData("savingMax",(savingMax + (res * 0.2).toInt()))
                saveData("savingProgress",(savingProgress + (res * 0.2).toInt()))
            }

            saveData("mainColor", R.drawable.custom_progress_bar)
            saveData("secondColor", R.drawable.custom_progress_bar)
            saveData("savingColor", R.drawable.custom_progress_bar2)

            binding.progressBarMain.progressDrawable = resources.getDrawable(
                pref.getInt("mainColor", R.drawable.custom_progress_bar))
            binding.progressBarSecondary.progressDrawable = resources.getDrawable(
                pref.getInt("secondColor", R.drawable.custom_progress_bar))
            binding.progressBarSaving.progressDrawable = resources.getDrawable(
                pref.getInt("savingColor", R.drawable.custom_progress_bar2))
        }
        thread.start()
        thread.join()

    }


}

// билдер теней
private class DragShadowBuilder(view: View, drawable: Int) : View.DragShadowBuilder(view) {

    private val shadow = ResourcesCompat.getDrawable(view.context.resources, drawable, view.context.theme)

    override fun onProvideShadowMetrics(size: Point, touch: Point) {

        val width: Int = view.width * 6 / 10
        val height: Int = view.height * 6 / 10

        shadow?.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow?.draw(canvas)
    }

}

class ProgressBarAnimation(
    private val progressBar: ProgressBar,
    private val from: Float,
    private val to: Float
) :
    Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
    }
}