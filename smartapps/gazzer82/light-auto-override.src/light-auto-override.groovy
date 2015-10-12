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
        required: true
    }
    section("Real Dimmer...") { 
        input "realdimmer", "capability.switchLevel", 
	title: "Real Dimmer...", 
        required: true
    }
    section("Virtual Stand-in...") {
    	input "standin", "capability.switch",
        title: "Stand In Virtual Switch...",
        required: true
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
    state.wasOff = realswitch.currentValue("switch") == "off"
    if(state.wasOff)realdimmer.setLevel(80,1)
    if(state.wasOff)realswitch.on()
}

def switchOffHandler(evt) {
    if(state.wasOff)realswitch.off()
}