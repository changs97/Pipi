package com.pipix.pipi.src.fragment.modify

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.ToggleButton
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.pipix.pipi.R
import com.pipix.pipi.config.ApplicationClass
import com.pipix.pipi.config.BaseFragment
import com.pipix.pipi.data.Old
import com.pipix.pipi.data.Webservice
import com.pipix.pipi.databinding.FragmentModifyBinding
import com.pipix.pipi.src.fragment.insertPerson.CustomDialog
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.dataList
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.friTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.friliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.monTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.monliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.recyclerviewAdapter
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.satTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.satliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.sunTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.sunliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.thuTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.thuliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.tuesTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.tuesliveChecked
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.wedTime
import com.pipix.pipi.src.fragment.insertPerson.InsertFragment.Companion.wedliveChecked
import com.pipix.pipi.src.fragment.insertPerson.SetTime
import com.pipix.pipi.src.fragment.insertPerson.model.InsertBody
import com.pipix.pipi.src.fragment.insertPerson.model.InsertScheduleBody
import com.pipix.pipi.src.fragment.insertPerson.model.InsertScheduleResponse
import com.pipix.pipi.src.fragment.modify.model.ModifyBody
import com.pipix.pipi.src.fragment.modify.model.ModifyResponse
import com.pipix.pipi.src.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModifyFragment : BaseFragment<FragmentModifyBinding>(FragmentModifyBinding::bind, R.layout.fragment_modify) {


    val IMAGE_PICK = 1111
    var selectImage: Uri?=null
    lateinit var storage: FirebaseStorage
    var genderType : Int? = null
    lateinit var complete : Button
    lateinit var name : EditText
    lateinit var address : EditText
    lateinit var age : EditText
    lateinit var radioGroup : RadioGroup
    lateinit var BtnMon : ToggleButton
    lateinit var BtnTues : ToggleButton
    lateinit var BtnWed : ToggleButton
    lateinit var BtnThu : ToggleButton
    lateinit var BtnFri : ToggleButton
    lateinit var BtnSat : ToggleButton
    lateinit var BtnSun : ToggleButton

    val args: ModifyFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.modifyImgbtnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        storage= FirebaseStorage.getInstance()

        val data =  args.myArg2

        viewBind()

        dataBind(data)




        monliveChecked.observe(viewLifecycleOwner, Observer {
            BtnMon.isChecked  = it
        })
        tuesliveChecked.observe(viewLifecycleOwner, Observer {
            BtnTues.isChecked  = it
        })
        wedliveChecked.observe(viewLifecycleOwner, Observer {
            BtnWed.isChecked  = it
        })
        thuliveChecked.observe(viewLifecycleOwner, Observer {
            BtnThu.isChecked  = it
        })
        friliveChecked.observe(viewLifecycleOwner, Observer {
            BtnFri.isChecked  = it
        })
        satliveChecked.observe(viewLifecycleOwner, Observer {
            BtnSat.isChecked  = it
        })
        sunliveChecked.observe(viewLifecycleOwner, Observer {
            BtnSun.isChecked  = it
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.modify_man -> {
                    genderType = 0
                    Log.d("성별","R.id.insert_man" + genderType.toString())
                }
                R.id.modify_woman -> {
                    genderType = 1
                    Log.d("성별","R.id.insert_woman" + genderType.toString())}
            }
        }



        BtnMon.setOnClickListener{
            if(BtnMon.isChecked) {
                CustomDialog(context as MainActivity, "월요일").show()
            } else BtnMon.isChecked = true }
        BtnTues.setOnClickListener{
            if(BtnTues.isChecked){
                CustomDialog(context as MainActivity, "화요일").show()
            } else BtnTues.isChecked = true }
        BtnWed.setOnClickListener{
            if(BtnWed.isChecked){
                CustomDialog(context as MainActivity, "수요일").show()

            } else BtnWed.isChecked = true }
        BtnThu.setOnClickListener{
            if(BtnThu.isChecked){
                CustomDialog(context as MainActivity, "목요일").show()
            } else BtnThu.isChecked = true
        }
        BtnFri.setOnClickListener{
            if(BtnFri.isChecked){
                CustomDialog(context as MainActivity, "금요일").show()
            } else BtnFri.isChecked = true
        }
        BtnSat.setOnClickListener{
            if(BtnSat.isChecked){
                CustomDialog(context as MainActivity, "토요일").show()
            } else BtnSat.isChecked = true
        }
        BtnSun.setOnClickListener{
            if(BtnSun.isChecked){
                CustomDialog(context as MainActivity, "일요일").show()
            } else BtnSun.isChecked = true
        }


        complete.setOnClickListener {
            complete.isEnabled = false
            if(name.text != null && age.text != null && genderType != null && address.text != null){

                showLoadingDialog(context as MainActivity)

                if(selectImage!=null) {
                    val fileName = "${binding.modifyEdittextName.text}${binding.modifyEdittextAge.text}.jpg"

                    val ref = storage.getReference().child("image").child(fileName)
                    val uploadTask = ref.putFile(selectImage!!)

                    val urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("insert테스트","이미지 URL 호출 성공")

                            val downloadUri = task.result
                            tryPutModify(
                                ModifyBody(address.text.toString(),age.text.toString(),
                                downloadUri.toString(),name.text.toString(),genderType!!)
                            ,data.oldID)

                            Glide.with(this)
                                .load(R.drawable.ic_basic_profile).centerCrop()
                                .into(binding.modifyCircleimageProfile)

                            dismissLoadingDialog()

                        } else {
                            // Handle failures
                            Log.d("insert테스트","이미지 업로드 실패")
                        }
                    }
                }
                else{

                    Log.d("성별",genderType!!.toString())
                    tryPutModify(
                        ModifyBody(address.text.toString(),age.text.toString(),
                           null,name.text.toString(),genderType!!)
                        ,data.oldID)

                    Glide.with(this)
                        .load(R.drawable.ic_basic_profile).centerCrop()
                        .into(binding.modifyCircleimageProfile)

                    dismissLoadingDialog()
                }
            }else {
                showCustomToast("필수 항목을 모두 입력하세요") }

            complete.isEnabled = true
        }

        val recyclerView = binding.modifyRecyclerview
        recyclerView.setLayoutManager(object : LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return false
            }
        })

        recyclerView.adapter = recyclerviewAdapter

        binding.modifyImgbtnChangeImage.setOnClickListener {
            var intent= Intent(Intent.ACTION_PICK) //선택하면 무언가를 띄움. 묵시적 호출
            intent.type="image/*"
            startActivityForResult(intent,IMAGE_PICK)
        }


    }


    fun tryPutModify(body : ModifyBody, patientId : Int){
        val UploadRetrofitInterface = ApplicationClass.sRetrofit.create(Webservice::class.java)
        UploadRetrofitInterface.putUpdate(body, patientId).enqueue(object :
            Callback<ModifyResponse> {
            override fun onResponse(
                call: Call<ModifyResponse>,
                response: Response<ModifyResponse>
            ) {
                if(response.isSuccessful()){
                    // 응답을 잘 받은 경우
                    val data = response.body() as ModifyResponse


                    MainActivity.viewModel.addOld(
                        Old( data.id, data.caregiverId.toString(), data.name, data.age, body.sex ,data.address, data.imageURL,
                            monTime, tuesTime, wedTime, thuTime, friTime, satTime, sunTime))

                    tryPutScheduleModify(InsertScheduleBody(friTime, monTime, satTime, sunTime, thuTime,
                        tuesTime, wedTime), data.id)
                }else{
                    //응답 실패 시 코드
                }


            }

            override fun onFailure(call: Call<ModifyResponse>, t: Throwable) {
                Log.d("tryPutModify",t.message ?:"통신 오류")
            }
        })
    }

    fun tryPutScheduleModify(body : InsertScheduleBody, patientId : Int){
        val UploadRetrofitInterface = ApplicationClass.sRetrofit.create(Webservice::class.java)
        UploadRetrofitInterface.putSchedule(body, patientId).enqueue(object :
            Callback<InsertScheduleResponse> {
            override fun onResponse(
                call: Call<InsertScheduleResponse>,
                response: Response<InsertScheduleResponse>
            ) {
                if(response.isSuccessful()){
                    Log.d("tryPutScheduleModify",response.body().toString())
                    val data = response.body() as InsertScheduleResponse
                    //all clear

                    dataClear()
                    findNavController().popBackStack()
                }


            }

            override fun onFailure(call: Call<InsertScheduleResponse>, t: Throwable) {
                Log.d("tryPutScheduleModify",t.message ?:"통신 오류")
            }
        })
    }

    fun viewBind() {
        complete = binding.modifyBtnComplate
        name = binding.modifyEdittextName
        address = binding.modifyEdittextAddress
        age = binding.modifyEdittextAge
        radioGroup = binding.modifyRadiogroup
        BtnMon = binding.modifyMon
        BtnTues = binding.modifyTues
        BtnWed = binding.modifyWed
        BtnThu = binding.modifyThu
        BtnFri = binding.modifyFri
        BtnSat = binding.modifySat
        BtnSun = binding.modifySun
    }

    fun dataBind(old: Old) {

        binding.modifyEdittextName.setText(old.oldName)
        binding.modifyEdittextAddress.setText(old.oldAddress)
        binding.modifyEdittextAge.setText(old.oldAge.toString())
        

        if (old.oldImage == null){
            binding.modifyCircleimageProfile.setImageResource(R.drawable.ic_basic_profile)}
        else{
            Glide.with(this)
                .load(old.oldImage.toString()).centerCrop()
                .into(binding.modifyCircleimageProfile) }
        when(old.oldSex){
            0 -> {
                binding.modifyMan.isChecked = true
                binding.modifyWoman.isChecked = false
                genderType = 0}
            1 -> {
                binding.modifyWoman.isChecked = true
                binding.modifyMan.isChecked = false
                genderType = 1}
        }


        if(old.mon != null){
            monTime = old.mon
            monliveChecked.value = true
            val setTime = old.mon.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"월요일"))
        }
        if(old.tue != null){
            tuesTime = old.tue
            tuesliveChecked.value = true
            val setTime = old.tue.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"화요일"))
        }
        if(old.wed != null){
            wedTime = old.wed
            wedliveChecked.value = true
            val setTime = old.wed.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"수요일"))
        }
        if(old.thu != null){
            thuTime = old.thu
            thuliveChecked.value = true
            val setTime = old.thu.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"목요일"))
        }
        if(old.fri != null){
            friTime = old.fri
            friliveChecked.value = true
            val setTime = old.fri.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"금요일"))
        }
        if(old.sat!= null){
            satTime = old.sat
            satliveChecked.value = true
            val setTime = old.sat.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"토요일"))
        }
        if(old.sun != null){
            sunTime = old.sun
            sunliveChecked.value = true
            val setTime = old.sun.split("-")
            val startTimeText = "${setTime[0]}:${setTime[1]}"
            val endTimeText = "${setTime[2]}:${setTime[3]}"
            dataList.add(SetTime(startTimeText,endTimeText,"일요일"))
        }



    }

    fun dataClear() {
        Log.d("modify","초기화")
        dataList.clear()
        recyclerviewAdapter.notifyDataSetChanged()
        name.text = null
        age.text = null
        address.text = null
        genderType = null
        radioGroup.clearCheck()
        monliveChecked.value = false
        tuesliveChecked.value = false
        wedliveChecked.value = false
        thuliveChecked.value = false
        friliveChecked.value = false
        satliveChecked.value = false
        sunliveChecked.value = false
        monTime = null
        tuesTime = null
        wedTime = null
        thuTime = null
        friTime = null
        satTime = null
        sunTime = null
    }

    override fun onDetach() {
        super.onDetach()
        dataClear()

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK&&resultCode== Activity.RESULT_OK){
            selectImage=data?.data
            binding.modifyCircleimageProfile.setImageURI(selectImage)
        }
    }

}



