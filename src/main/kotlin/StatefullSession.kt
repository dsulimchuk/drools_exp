package com.ds

import org.kie.api.KieServices
import java.util.*


fun main() {
    val kieServices = KieServices.Factory.get()
    val kContainer = kieServices.getKieClasspathContainer()
    val session = kContainer.newKieSession()

    val names = arrayOf("kitchen", "bedroom", "office", "livingroom")
    val name2room = HashMap<String, Room>()
    for (name in names) {
        val room = Room(name)
        name2room[name] = room
        session.insert(room)
        val sprinkler = Sprinkler(room)
        session.insert(sprinkler)
    }

    session.fireAllRules()

    val kitchenFire = Fire(name2room["kitchen"]!!)
    val officeFire = Fire(name2room["office"]!!)

    val kitchenFireHandle = session.insert(kitchenFire)
    val officeFireHandle = session.insert(officeFire)

    session.fireAllRules()

    session.delete( kitchenFireHandle );
    session.delete( officeFireHandle );

    session.fireAllRules()
}

data class Room(val name: String)

data class Sprinkler(val room: Room, var on: Boolean = false)

data class Fire(val room: Room)

class Alarm