package com.example.wym_002.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wym_002.R
import com.example.wym_002.databinding.CustomDialogLayoutBinding
import com.example.wym_002.databinding.DialogDeleteBinding
import com.example.wym_002.databinding.DialogInfoBinding
import com.example.wym_002.databinding.DialogScrollDateBinding
import com.example.wym_002.databinding.FragmentSettingsFragmentBinding
import com.example.wym_002.db.MainDb
import com.example.wym_002.hidingPanel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsFragmentBinding
    private lateinit var dialog: Dialog
    private lateinit var dialogScrollDate: DialogScrollDateBinding
    private lateinit var dialogMyInfo: DialogInfoBinding
    private lateinit var dialogDelete: DialogDeleteBinding
    lateinit var pref: SharedPreferences

    var toast: Toast? = null

    lateinit var db: MainDb

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsFragmentBinding.inflate(layoutInflater)

        db = MainDb.getDb(this.requireActivity())

        pref = requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
        // resultCardBalance        key: R.drawable.credit_card_white.toString()
        // resultWalletBalance      key: R.drawable.wallet_white.toString()
        // resultBankBalance        key: R.drawable.account_balance_white.toString()
        //
        // checkSplashScreen        key: getString(R.string.flagSplashScreen)      ОБРАТНЫЕ ПЕРЕМЕННЫЕ
        //                                                                        0 == TRUE   1 == FALSE
        // setDateDay               key: getString(R.string.setDateDay)


        binding.switchSplashScreen.isChecked = pref.getInt(getString(R.string.flagSplashScreen), 0) == 0
        updatingVariables()

        buttonToInfo()

        buttonToClear()

        switchChecked()

        buttonToDate()

        return binding.root
    }

    private fun buttonToInfo() {

        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.textInfo.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogMyInfo()

        }

        binding.imageInfo.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogMyInfo()

        }

    }

    private fun showDialogMyInfo() {

        dialogMyInfo = DialogInfoBinding.inflate(layoutInflater)
        dialog = Dialog(this.requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogMyInfo.root)
        dialog.setCancelable(true)

        dialogMyInfo.dialogBtn.setOnClickListener {
            dialog.dismiss()
        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun buttonToDate() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.textData.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogSetDate()

        }

        binding.imageDate.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogSetDate()

        }

        binding.textSetDate.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogSetDate()

        }

    }

    private fun showDialogSetDate() {    // показывает диалоговое окно для выбора  даты

        dialogScrollDate = DialogScrollDateBinding.inflate(layoutInflater)
        dialog = Dialog(this.requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogScrollDate.root)
        dialog.setCancelable(true)

        dialogScrollDate.numberPicker.minValue = 1
        dialogScrollDate.numberPicker.maxValue = 31
        dialogScrollDate.numberPicker.value = pref.getInt(getString(R.string.setDateDay), 0) + 1
        dialogScrollDate.numberPicker.wrapSelectorWheel = false

        var res = pref.getInt(getString(R.string.setDateDay), 0)  // день начала учета

        dialogScrollDate.numberPicker.setOnValueChangedListener{ _, _, newVal ->
            res = newVal - 1
        }

        dialogScrollDate.dialogBtn.setOnClickListener {

            // СОХРАНЯЕТСЯ ДЕНЬ НАЧАЛА УЧЕТА!!!
            saveData(getString(R.string.setDateDay), res)
            dialog.dismiss()
            val setDate = pref.getInt(getString(R.string.setDateDay), 0) + 1
            binding.textSetDate.text = setDate.toString()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun switchChecked() {        // включение заставки

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70


        binding.textSpashScreen.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            when (binding.switchSplashScreen.isChecked) {
                true -> {
                    binding.switchSplashScreen.isChecked = false
                    saveData(getString(R.string.flagSplashScreen), 1)
                }
                else -> {
                    binding.switchSplashScreen.isChecked = true
                    saveData(getString(R.string.flagSplashScreen), 0)
                }
            }
        }

        binding.imageSplashScreen.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            when (binding.switchSplashScreen.isChecked) {
                true -> {
                    binding.switchSplashScreen.isChecked = false
                    saveData(getString(R.string.flagSplashScreen), 1)
                }
                else -> {
                    binding.switchSplashScreen.isChecked = true
                    saveData(getString(R.string.flagSplashScreen), 0)
                }
            }
        }

        binding.switchSplashScreen.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                saveData(getString(R.string.flagSplashScreen), 0)
            }else{
                saveData(getString(R.string.flagSplashScreen), 1)
            }
        }
    }

    private fun buttonToClear() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.clearPref.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogDelete()

        }

        binding.imageClearPref.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogDelete()

        }

    }

    private fun showDialogDelete() {

        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogDelete = DialogDeleteBinding.inflate(layoutInflater)
        dialog = Dialog(this.requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogDelete.root)
        dialog.setCancelable(true)

        dialogDelete.dialogBtnYes.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showToastMsg(getString(R.string.deleteData))

            Thread{
                db.getDao().deleteAll()
            }.start()

            clearPref()
            updatingVariables()
            dialog.dismiss()

        }

        dialogDelete.dialogBtnCancel.setOnClickListener {

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun clearPref() {    // очищает переменные в памяти
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveData(key: String, dataToSave: Int) {

        val editor = pref.edit()
        editor?.putInt(key, dataToSave)
        editor?.apply()

    }

    private fun updatingVariables() {       // обновляет счетчики на экране

        val setDate = pref.getInt(getString(R.string.setDateDay), 0) + 1
        binding.textSetDate.text = setDate.toString()

    }

    private fun showToastMsg(string: String) {

        val container = this.requireActivity().findViewById<ViewGroup>(R.id.custom_toast_container)
        val layout = layoutInflater.inflate(R.layout.custom_toast, container)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = string

        if (toast != null){
            toast!!.cancel()
        }
        toast = Toast(this.requireActivity().applicationContext)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.view = layout
        toast!!.show()

    }

}