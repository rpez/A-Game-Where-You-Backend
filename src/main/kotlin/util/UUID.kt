package com.rekon.util

import kotlin.uuid.Uuid

fun getNewUuid(): Uuid {
    return Uuid.random();
}