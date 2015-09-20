/**
 *  Arduino
 *
 *  Copyright 2014 Daniel Ogorchock
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
 *  
 *
 *  Based on 
 *  	Garage Door
 *  	Original Author: Steve Sell
 *  	Date: 2014-02-02
 *
 *  Modified and Extended for my personal use 
 * 	Author: Dan Ogorchock
 *      Date: 2014-07-05
 */

metadata {
	// Automatically generated. Make future change here.
	definition (name: "Arduino", namespace: "gazzer82", author: "Gareth Jeanne") {
		capability "Polling"
        	capability "Refresh"
        	capability "Contact Sensor"

        	attribute "frontDoor", "string"
        	attribute "patioLDoor", "string"
        	attribute "patioRDoor", "string"
	}

    simulator {
        status "on":  "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6E"
        status "off": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6666"

        // reply messages
        reply "raw 0x0 { 00 00 0a 0a 6f 6e }": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6E"
        reply "raw 0x0 { 00 00 0a 0a 6f 66 66 }": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6666"
    }
	
    // Preferences


	// tile definitions
	tiles {
        standardTile("frontDoor", "device.frontDoor", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#ffa81e")
			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#79b821")
 		}
        standardTile("patioLDoor", "device.patioLDoor", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#ffa81e")
			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#79b821")
 		}
        standardTile("patioRDoor", "device.patioRDoor", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#ffa81e")
			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#79b821")
 		}

		main "frontDoor"
        details(["frontDoor","patioLDoor","patioRDoor"])
	}
}

//Map parse(String description) {
def parse(String description) {
	def name = null
	def value = zigbee.parse(description)?.text
    log.debug "Value is ${value}"
	def linkText = getLinkText(device)
	def descriptionText = getDescriptionText(description, linkText, value)
	def handlerName = value
	def isStateChange = value != "ping"
	def displayed = value && isStateChange

    def incoming_cmd = value.split()

    name = incoming_cmd[0]
    value = incoming_cmd[1]
	
	def result = [
		value: value,
        name: value != "ping" ? name : null,
		handlerName: handlerName,
		linkText: linkText,
		descriptionText: descriptionText,
		isStateChange: isStateChange,
		displayed: displayed
	]
 	log.debug result
	return result   

}