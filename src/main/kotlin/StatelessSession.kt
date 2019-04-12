package com.ds

import org.kie.api.KieServices
import java.util.*


fun main() {
    val kieServices = KieServices.Factory.get()
    val kContainer = kieServices.getKieClasspathContainer()
    val session = kContainer.newStatelessKieSession()

    Random(0)
        .ints(10, 40)
        .limit(10)
        .forEach {
            val ctx = Context(Applicant("$it", it))
            session.execute(ctx)
            println(ctx)
        }
}

data class Context<T>(
    val any: T,
    val conflicts: List<ActionConflict> = mutableListOf()
)

data class Applicant(
    var name: String,
    var age: Int,
    var valid: Boolean? = null
)

data class ActionConflict(
    val message: String
)