package com.harryliu.carlie.firebaseModels


/**
 * @author Harry Liu
 *
 * @version Feb 26, 2018
 */

class TripModel : FirebaseModel {

    var passengerId: String? = null
        set(value) {
            if (value != null)
                updateProperty?.invoke("passengerId", value)
            field = value
        }

    var pickupLocation: LocationModel? = null

    var dropOffLocation: LocationModel? = null

    var shuttleId: String? = null
        set(value) {
            if (value != null)
                updateProperty?.invoke("shuttleId", value)
            field = value
        }
    var shuttleEntered: Boolean = false
        set(value) {
            updateProperty?.invoke("shuttleEntered", value)
            field = value
        }
    var passengerLeft: Boolean = false
        set(value) {
            updateProperty?.invoke("passengerLeft", value)
            field = value
        }
    var cancel: Boolean = false
        set(value) {
            updateProperty?.invoke("cancel", value)
            field = value
        }

    var pickupTime: Long? = null
        set(value) {
            if (value != null)
                updateProperty?.invoke("pickupTime", value)
            field = value
        }


    constructor()

    constructor(passenger: String, pickupLocationValue: RealTimeValue<LocationModel>, dropOffLocationValue: RealTimeValue<LocationModel>, shuttleId: String) {
        this.passengerId = passenger
        this.pickupLocation = pickupLocationValue.getValue()
        this.dropOffLocation = dropOffLocationValue.getValue()
        this.shuttleId = shuttleId

        pickupLocationValue.onChange
                .subscribe { value ->
                    pickupLocation = value
                    onAttributeChange.onNext(Pair("pickupLocation", value))
                }
        dropOffLocationValue.onChange
                .subscribe { value ->
                    dropOffLocation = value
                    onAttributeChange.onNext(Pair("dropOffLocation", value))
                }
    }

    override fun toMap(): Map<String, Any> {
        return hashMapOf(
                "pickupLocation" to pickupLocation!!.toMap(),
                "dropOffLocation" to dropOffLocation!!.toMap(),
                "passengerId" to passengerId!!,
                "shuttleId" to shuttleId!!,
                "shuttleEntered" to shuttleEntered,
                "passengerLeft" to passengerLeft
        )
    }

    override fun toString(): String {
        return """
            TripModel
                passengerId=$passengerId
                pickupLocation=$pickupLocation
                dropOffLocation=$dropOffLocation
                shuttleId=$shuttleId
                shuttleEntered=$shuttleEntered
                passengerLeft=$passengerLeft
                """
    }

    override fun updatePropertyListeners(): Map<String, Pair<IsPropertyUpdatedChecker, UpdatePropertyOperation>> {
        return mapOf(
                "passengerId" to Pair({ newValue -> newValue != passengerId }, { newValue -> passengerId = newValue as String })
        )
    }
}