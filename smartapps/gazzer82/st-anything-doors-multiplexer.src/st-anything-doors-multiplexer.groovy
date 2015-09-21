/**
 *  ST_Anything Doors Multiplexer - ST_Anything_Doors_Multiplexer.smartapp.groovy
 *
 *  Copyright 2015 Daniel Ogorchock
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Change History:
 *
 *    Date        Who            What
 *    ----        ---            ----
 *    2015-01-10  Dan Ogorchock  Original Creation
 *    2015-01-11  Dan Ogorchock  Reduced unnecessary chatter to the virtual devices
 *    2015-01-18  Dan Ogorchock  Added support for Virtual Temperature/Humidity Device
 *    2015-01-18  Gareth Jeanne  Modified code to only use my temperature sensors
 */
 
definition(
    name: "ST_Anything Doors Multiplexer",
    namespace: "gazzer82",
    author: "Gareth Jeanne",
    description: "Connects single Arduino with multiple ContactSensor devices to their virtual device counterparts.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {

	section("Select the House Doors (Virtual Contact Sensor devices)") {
		input ("frontdoor", title: "Virtual Contact Sensor for Front Door", "capability.contactSensor")
		input ("patioldoor", title: "Virtual Contact Sensor for Left Patio Door", "capability.contactSensor")
		input ("patiordoor", title: "Virtual Contact Sensor for Right Patio Door", "capability.contactSensor")
        input ("allpatiodoor", title: "Virtual Contact Sensor for all Patio Doors", required: "false", "capability.contactSensor")
		input ("alldoor", title: "Virtual Contact Sensor for all Doors", required: "false", "capability.contactSensor")
	}

	section("Select the Arduino ST_Anything_Doors device") {
		input "arduino", "capability.contactSensor"
    }    
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe()
}

def subscribe() {
    
    subscribe(arduino, "frontDoor.open", frontDoorOpen)
    subscribe(arduino, "frontDoor.closed", frontDoorClosed)
    
    subscribe(arduino, "patioLDoor.open", patioLDoorOpen)
    subscribe(arduino, "patioLDoor.closed", patioLDoorClosed)

    subscribe(arduino, "patioRDoor.open", patioRDoorOpen)
    subscribe(arduino, "patioRDoor.closed", patioRDoorClosed)
}

// --- Front Door --- 
def frontDoorOpen(evt)
{
    if (frontdoor.currentValue("contact") != "open") {
    	log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	frontdoor.openme()
        openVirtual()
    }
}

def frontDoorClosed(evt)
{
    if (frontdoor.currentValue("contact") != "closed") {
		log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	frontdoor.closeme()
        closeVirtual()
    }
}

// --- Patio left Door --- 
def patioLDoorOpen(evt)
{
    if (patioldoor.currentValue("contact") != "open") {
		log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	patioldoor.openme()
        openVirtual()
    }
}

def patioLDoorClosed(evt)
{
    if (patioldoor.currentValue("contact") != "closed") {
		log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	patioldoor.closeme()
        closeVirtual()
	}
}

// --- Patio right Door --- 
def patioRDoorOpen(evt)
{
    if (patiordoor.currentValue("contact") != "open") {
		log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	patiordoor.openme()
        openVirtual()
    }
}

def patioRDoorClosed(evt)
{
    if (patiordoor.currentValue("contact") != "closed") {
		log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	patiordoor.closeme()
        closeVirtual()
	}
}

def closeVirtual()
{
	log.debug "Virtual closed called"
    allpatiodoor.closeme()
    if(patiordoor.currentValue("contact") == "closed" && patioldoor.currentValue("contact") == "closed"){
    	log.debug "All patio doors closed"
    	allpatiodoor.closeme()
    }
    if(patiordoor.currentValue("contact") == "closed" && patioldoor.currentValue("contact") == "closed" && frontdoor.currentValue("contact") == "closed"){
    	log.debug "All doors closed called"
        alldoor.closeme()
    }
}

def openVirtual()
{
	log.debug "Virtual open called"
    allpatiodoor.closeme()
    if(patiordoor.currentValue("contact") == "open" || patioldoor.currentValue("contact") == "open"){
    	log.debug "Only patio doors open"
    	allpatiodoor.openme()
    }
    if(patiordoor.currentValue("contact") == "open" || patioldoor.currentValue("contact") == "open" || frontdoor.currentValue("contact") == "open"){
    	log.debug "One of the doors open"
        alldoor.openme()
    }
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}