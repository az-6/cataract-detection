package com.dicoding.capstone.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dicoding.capstone.R
import com.dicoding.capstone.ui.onboarding.OnBoardingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // Inisialisasi Firestore dan FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    // UI components
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUid: TextView
    private lateinit var btnLogout: Button // Tombol Logout
    private lateinit var btnDelete: Button // Tombol Delete Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi Firestore dan FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi TextViews untuk menampilkan data
        tvName = rootView.findViewById(R.id.tvName)
        tvEmail = rootView.findViewById(R.id.tvEmail)
        tvUid = rootView.findViewById(R.id.tvUid)
        btnLogout = rootView.findViewById(R.id.btnLogout) // Bind tombol logout
        btnDelete = rootView.findViewById(R.id.btnDelete) // Bind tombol delete

        // Ambil UID dari Firebase Authentication
        val user = mAuth.currentUser
        if (user != null) {
            // Ambil data pengguna dari Firestore menggunakan UID
            getUserDataFromFirestore(user.uid)
        } else {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show()
        }

        // Set listener untuk tombol logout
        btnLogout.setOnClickListener {
            logoutUser()
        }

        // Set listener untuk tombol delete account
        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        return rootView
    }

    // Fungsi untuk mengambil data pengguna dari Firestore
    private fun getUserDataFromFirestore(uid: String) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val email = document.getString("email")

                    // Tampilkan data pengguna di TextViews
                    tvName.text = name ?: "Name not available"
                    tvEmail.text = email ?: "Email not available"
                    tvUid.text = uid
                } else {
                    Toast.makeText(requireContext(), "User data not found in Firestore", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to get user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Fungsi untuk logout tanpa menghapus akun
    private fun logoutUser() {
        val user = mAuth.currentUser

        if (user != null) {
            // Logout pengguna
            mAuth.signOut()

            // Tampilkan pesan sukses
            Toast.makeText(requireContext(), "Successfully logged out", Toast.LENGTH_SHORT).show()

            // Setelah logout, arahkan pengguna ke halaman OnBoarding
            navigateToOnBoarding()
        } else {
            Toast.makeText(requireContext(), "No user is logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk menavigasi ke OnBoardingActivity
    private fun navigateToOnBoarding() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Finish current activity to remove it from the back stack
    }

    // Fungsi untuk menghapus akun
    private fun deleteUserAccount() {
        val user = mAuth.currentUser
        if (user != null) {
            // Hapus data pengguna dari Firestore
            firestore.collection("users").document(user.uid)
                .delete()
                .addOnSuccessListener {
                    // Setelah data pengguna dihapus, hapus akun Firebase Authentication
                    user.delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Account successfully deleted", Toast.LENGTH_SHORT).show()
                            // Arahkan ke halaman OnBoarding setelah penghapusan akun
                            navigateToOnBoarding()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed to delete account: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to delete user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk menampilkan konfirmasi sebelum menghapus akun
    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account? This action is irreversible.")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteUserAccount()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
}
