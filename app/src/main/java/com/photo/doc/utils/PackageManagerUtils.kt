package com.photo.doc.utils

import com.google.common.io.BaseEncoding
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.annotation.NonNull
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object PackageManagerUtils {

    fun getSignature(@NonNull pm: PackageManager, @NonNull packageName: String): String? {
        try {
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            return if (packageInfo?.signatures == null
                    || packageInfo.signatures.isEmpty()
                    || packageInfo.signatures[0] == null) {
                null
            } else signatureDigest(packageInfo.signatures[0])
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }

    }

    private fun signatureDigest(sig: Signature): String? {
        val signature = sig.toByteArray()
        return try {
            val md = MessageDigest.getInstance("SHA1")
            val digest = md.digest(signature)
            BaseEncoding.base16().lowerCase().encode(digest)
        } catch (e: NoSuchAlgorithmException) {
            null
        }

    }
}