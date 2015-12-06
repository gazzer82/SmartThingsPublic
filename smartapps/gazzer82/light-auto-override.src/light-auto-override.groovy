/**
 *  Light Auto Override
 *
 *  Copyright 2015 Gareth  Jeanne
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
 */
definition(
    name: "Light Auto Override",
    namespace: "gazzer82",
    author: "Gareth  Jeanne",
    description: "App that prevents a light being auto turned off if it's already turned on.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Real Switch...") { 
        input "realswitch", "capability.switch", 
	title: "Real Switch...", 
        required: true,
        multiple: true
    }
    section("Real Dimmer...") { 
        input "realdimmer", "capability.switchLevel", 
	title: "Real Dimmer...", 
        required: false,
        multiple: true
    }
    section("Virtual Stand-in...") {
    	input "standin", "capability.switch",
        title: "Stand In Virtual Switch...",
        required: true
    }
    section("Day Modes ...") {
        input      "dayModes", "mode",
        title:      "Modes for day mode...",
        multiple:   true,
        required:   true
    }
    section("Night Modes ...") {
        input      "nightModes", "mode",
        title:      "Modes for night mode...",
        multiple:   true,
        required:   true
    }
    section("Day Level"){
    	input "dayLevel", "enum",
        title: "Dimmer level for day",
        options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],
        required: false
    }
    section("Night Level"){
    	input "nightLevel", "enum",
        title: "Dimmer level for night",
        options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]],
        required: false
    }
}

def installed() {
    subscribe(standin, "switch.on", switchOnHandler)
    subscribe(standin, "switch.off", switchOffHandler)
}

def updated() {
    unsubscribe()
    subscribe(standin, "switch.on", switchOnHandler)
    subscribe(standin, "switch.off", switchOffHandler)
}

def switchOnHandler(evt) {
    state.wasOff = false
    for (thisswitch in realswitch) {
    	if(thisswitch.currentValue("switch") == "off"){
        	state.wasOff = true
        }
    }
    log.debug "Switch state is: ${state.wasOff}"
    if (dayModes.contains(location.mode)) {
    	log.debug "Setting daytime light to: ${dayLevel}"
        def dayCleanValue = dayLevel as Integer
        for (thisdimmer in realdimmer) {
        	log.debug "Setting dimmer for: ${thisdimmer}"
    		if(state.wasOff)thisdimmer.setLevel(dayCleanValue)
       	}
        for (thisswitch in realswitch) {
        	log.debug "Turning on: ${thisswitch}"
    		if(state.wasOff)thisswitch.on()
       	}
    } else if (nightModes.contains(location.mode)){
    	log.debug "Setting nightime light to: ${nightLevel}"
        def nightCleanValue = nightLevel as Integer
    	for (thisdimmer in realdimmer) {
        	log.debug "Setting dimmer for: ${thisdimmer}"
    		if(state.wasOff)thisdimmer.setLevel(nightCleanValue)
       	}
        for (thisswitch in realswitch) {
        	log.debug "Turning on: ${thisswitch}"
    		if(state.wasOff)thisswitch.on()
       	}
    }
}

def switchOffHandler(evt) {
	for (thisswitch in realswitch) {
        	log.debug "Turning off: ${thisswitch}"
    		if(state.wasOff)thisswitch.off()
    }
}