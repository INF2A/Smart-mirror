package UnitTest;/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioListenExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;

import java.util.ArrayList;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the RaspberryPi.
 *
 * @author Robert Savage
 */
public class GpioListenExample {

    public GpioListenExample(String[] args) throws InterruptedException, PlatformAlreadyAssignedException
    {
    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * [ARGUMENT/OPTION "--pull (up|down|off)" | "-l (up|down|off)" | "--up" | "--down" ]
     * This example program accepts an optional argument for specifying pin pull resistance.
     * Supported values: "up|down" (or simply "1|0").   If no value is specified in the command
     * argument, then the pin pull resistance will be set to PULL_UP by default.
     * -- EXAMPLES: "--pull up", "-pull down", "--pull off", "--up", "--down", "-pull 0", "--pull 1", "-l up", "-l down".
     *
     * @param args
     * @throws InterruptedException
     * @throws PlatformAlreadyAssignedException
     */

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
       // final Console console = new Console();

        // print program title/header
     //   console.title("<-- The Pi4J Project -->", "GPIO Listen Example");

        // allow for user to exit program using CTRL-C
      //  console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();


        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin0 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_00, args);
        Pin pin1 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_01, args);
        Pin pin2 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_02, args);
        Pin pin3 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_03, args);

//        Pin pin2 = CommandArgumentParser.getPin(
//                RaspiPin.class,    // pin provider class to obtain pin instance from
//                RaspiPin.GPIO_02,  // default pin if no pin argument found
//                args);             // argument array to search in


        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP,  // default pin pull resistance if no pull argument found
                args);                      // argument array to search in

        // provision gpio pins as an system.input pin with its internal pull resistor set to UP or DOWN
        final GpioPinDigitalInput buttonLeft    = gpio.provisionDigitalInputPin(pin0, pull);
        final GpioPinDigitalInput buttonRight   = gpio.provisionDigitalInputPin(pin1, pull);
        final GpioPinDigitalInput buttonBack    = gpio.provisionDigitalInputPin(pin2, pull);
        final GpioPinDigitalInput buttonMenu    = gpio.provisionDigitalInputPin(pin3, pull);


        ArrayList<GpioPinDigitalInput> gpioPins = new ArrayList<>();
        gpioPins.add(buttonLeft);
        gpioPins.add(buttonRight);
        gpioPins.add(buttonBack);
        gpioPins.add(buttonMenu);

        // unexport the GPIO pins when program exits
        for (GpioPinDigitalInput pin : gpioPins) {
            pin.setShutdownOptions(true);

            // create and register gpio pin listener
            pin.addListener(new GpioPinListenerDigital() {
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    event.getState().isHigh();
                }
            });
        }


        // prompt user that we are ready
//        console.println(" ... Successfully provisioned [" + pin + "] with PULL resistance = [" + pull + "]");
//        console.emptyLine();
//        console.box("Please complete the [" + pin + "] circuit and see",
//                "the listener feedback here in the console.");
//        console.emptyLine();


        // create and register gpio pin listener
//        myButton2.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                // display pin state on console
//                console.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " +
//                        ConsoleColor.conditional(
//                                event.getState().isHigh(), // conditional expression
//                                ConsoleColor.GREEN,        // positive conditional color
//                                ConsoleColor.RED,          // negative conditional color
//                                event.getState()));        // text to display
//            }
//
//        });


        // forcefully shutdown all GPIO monitoring threads and scheduled tasks
        gpio.shutdown();
    }
}
