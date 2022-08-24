package com.example.kafiesta.screens.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.constants.ServerConst.IS_CLIENT
import com.example.kafiesta.constants.ServerConst.IS_SUCCESS
import com.example.kafiesta.databinding.ActivityLoginBinding
import com.example.kafiesta.screens.BaseActivity
import com.example.kafiesta.screens.main.MainActivity
import com.example.kafiesta.utilities.extensions.hideView
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.extensions.showView
import com.example.kafiesta.utilities.hideKeyboard
import com.example.kafiesta.utilities.initMultiplePermission
import timber.log.Timber

class LoginActivity : BaseActivity() {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = false

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    /**
     * Call all the functions here that needed to be initialized first
     */
    private fun initConfig() {
        initHideActionBar()
        initBinding()
        initMultiplePermission(this)
        initListener()
        initLiveData()
    }

    /**
     * Hide action bar because it is a splash screen
     */
    private fun initHideActionBar() {
        supportActionBar?.hide()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(LoginViewModel::class.java)

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                @Suppress("DEPRECATION")
                binding.textLayoutPassword.isPasswordVisibilityToggleEnabled =
                    binding.etPassword.text.toString().isNotEmpty()
            }
        })

        binding.etPassword.setImeActionLabel(
            getString(R.string.action_label_login),
            EditorInfo.IME_ACTION_DONE
        )

        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                initiateLogin()
                true
            } else {
                false
            }
        }
    }

    /**
     * Initialize event listeners
     */
    private fun initListener() {
        binding.buttonLogin.setOnClickListener {
            initiateLogin()
        }
    }

    /**
     * Initialize the live dataResult to be observe on this activity
     */
    private fun initLiveData() {
        //Observe `userDomain` live dataResult from our LoginViewModel
        viewModel.userDomain.observe(this) { userDomain ->
            //if the user.status == "success" and role == "client" we can now proceed to the MainActivity activity
            if (userDomain.status.matches(IS_SUCCESS.toRegex()) && userDomain.data.profile.role.matches(
                    IS_CLIENT.toRegex())
            ) {
                //proceedToActivity function can be found at BaseActivity.kt
                proceedToActivity(MainActivity::class.java, true)
            } else {
                viewModel.setLoading(false)
                showToast(getString(R.string.login_something_went_wrong))
            }
        }

        viewModel.isLoading.observe(this) {
            setLoading(it)
        }

        //Observe `loginFormState` live dataResult from our LoginViewModel
        viewModel.loginFormState.observe(this) { formState ->
            //setLoading function will be called here
            setLoading(formState.isLoading)
            //setValidation function will be called here
            setValidation(
                formState.isInvalidEmail, formState.invalidEmailMessage,
                formState.isInvalidPassword, formState.invalidPasswordMessage
            )
        }

        //Observe `networkFormStateLogin` live dataResult from our LoginViewModel
        viewModel.networkFormState.observe(this) {
            //it: NetworkFormStateLogin class
            if (it.serverError) {
                Timber.d(it.message)
                viewModel.setLoading(false)
                showToast(getString(R.string.login_something_went_wrong))
            }
        }
    }

    private fun setLoading(set: Boolean) {
        //Wrap it in try and catch block to avoid exceptions when hiding and showing a views
        //This catch exception will occur when the user changes from this activity to another
        try {
            if (set) {
                //showView function will saw it on Context.kt class to show the view
                //hideView function will saw it on Context.kt class to hide the view
                showView(binding.progressLoading)
                hideView(binding.buttonLogin)
            } else {
                hideView(binding.progressLoading)
                showView(binding.buttonLogin)

            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    /**
     * In  this function will set the validation of the text view username and password given from our LoginViewModel
     *
     * @param isInvalidEmail boolean to show the @invalidUsernameMessage dataResult on text view error
     * @param invalidEmailMessage string dataResult on text view error
     * @param isInvalidPassword boolean to show the @invalidPasswordMessage dataResult on text view error
     * @param invalidPasswordMessage string dataResult on text view error
     */
    private fun setValidation(
        isInvalidEmail: Boolean, invalidEmailMessage: String,
        isInvalidPassword: Boolean, invalidPasswordMessage: String,
    ) {
        binding.apply {
            //if both isInvalidUsername and isInvalidPassword are true
            //we will set an error given from @param invalidUsernameMessage and @param invalidPasswordMessage
            //from both text view textEditUser and textEditPassword
            if (isInvalidEmail && isInvalidPassword) {
                etEmail.error = invalidEmailMessage
                etPassword.error = invalidPasswordMessage
            }
            //if isInvalidUsername is true
            //we will set an error text given from @param invalidUsernameMessage
            //to this text view textEditUser
            else if (isInvalidEmail) {
                etEmail.error = invalidEmailMessage
            }
            //if isInvalidPassword is true
            //we will set an error text given from @param invalidPasswordMessage
            //to this text view textEditPassword
            else if (isInvalidPassword) {
                etPassword.error = invalidPasswordMessage
            }
        }
    }

    /**
     * In this function will initiate the login of the user
     */
    private fun initiateLogin() {
        //Hide the keyboard first for the user experience
        hideKeyboard(this)
        //Get the user name and password on this lines of code inputted by user
        val username = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
//        viewModel.validateLoginCredentials(this, "jadalmario.freelancer@gmail.com", "@Unknown0322") // Admin Account
//        viewModel.validateLoginCredentials(this, "client@email.com", "@Unknown0322") // Client Account
        viewModel.validateLoginCredentials(this, username, password, true)
    }
}