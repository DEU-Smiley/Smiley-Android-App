package com.example.smiley.common.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashManger {

    enum class Tag {
        /* 필요한 태그를 지정해서 사용 */
    }

    companion object {

        /**
         * Crash 발생시 별도로 로그를 기록
         * 기록된 로그는 비정상종료가 발생하면 Crashlytics dashboard '로그' 탭에서 확인 가.
         */
        fun log(tag: Tag, message: String) {
            FirebaseCrashlytics.getInstance().log("[${tag.name}] $message")
        }

        /**
         * 발생한 에러를 '심각하지 않음' 수준으로 기록
         * 기록된 에러는 캐싱되어 있다가, '비정상적으로 종료' 되는 순간 함께 Firebase에서 확인 가능
         */
        fun record(throwable: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        }
    }
}