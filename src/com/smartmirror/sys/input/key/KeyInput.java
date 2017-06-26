package com.smartmirror.sys.input.key;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.pi4j.util.ConsoleColor;
import com.smartmirror.core.input.IKeyHandler;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Erwin on 5/15/2017.
 */
public class KeyInput {

    // Key handler to pass the events through when a pin changes its state
    IKeyHandler keyHandler;


    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();


    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    final Console console = new Console();

    public KeyInput(IKeyHandler keyHandler) throws InterruptedException, PlatformAlreadyAssignedException {
        this.keyHandler = keyHandler;


        // print program title/header
        console.title("<-- The Pi4J Project -->", "GPIO Listen Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin0 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_00);
        Pin pin1 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_01);
        Pin pin2 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_02);
        Pin pin3 = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_03);

        //        Pin pin2 = CommandArgumentParser.getPin(
//                RaspiPin.class,    // pin provider class to obtain pin instance from
//                RaspiPin.GPIO_02,  // default pin if no pin argument found
//                args);             // argument array to search in


        // by default we will use gpio pin PULL-UP; however, if an argument
        // has been provided, then use the specified pull resistance
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                PinPullResistance.PULL_UP);

        // provision gpio pins as an system.input pin with its internal pull resistor set to UP or DOWN
        final GpioPinDigitalInput buttonLeft  = gpio.provisionDigitalInputPin(pin0, pull);
        final GpioPinDigitalInput buttonRight = gpio.provisionDigitalInputPin(pin1, pull);
        final GpioPinDigitalInput buttonBack  = gpio.provisionDigitalInputPin(pin2, pull);
        final GpioPinDigitalInput buttonMenu  = gpio.provisionDigitalInputPin(pin3, pull);

//        buttonLeft.setShutdownOptions(true);
//        buttonRight.setShutdownOptions(true);
//        buttonBack.setShutdownOptions(true);
//        buttonMenu.setShutdownOptions(true);
//
//        // create and register gpio pin listener
//        buttonLeft.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                if (event.getState().isHigh()) pinHigh();
//                else pinLow();
//            }
//        });
//
//
//        buttonRight.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                if (event.getState().isHigh()) pinHigh();
//                else pinLow();
//            }
//        });

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
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    console.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " +
                        ConsoleColor.conditional(
                                event.getState().isHigh(), // conditional expression
                                ConsoleColor.GREEN,        // positive conditional color
                                ConsoleColor.RED,          // negative conditional color
                                event.getState()));        // text to display

                    if (event.getState().isHigh()) {
                        pinHigh();

                        java.lang.System.out.println(event.getPin().getPin());

                        if(event.getPin().getPin().equals(pin0)) {
                            SwingUtilities.invokeLater(() -> keyHandler.onMenuButton());
                        }
                        else if(event.getPin().getPin().equals(pin1)) {
                            SwingUtilities.invokeLater(() -> keyHandler.onLeftButton());
                        }

                        else if(event.getPin().getPin().equals(pin2)) {
                            SwingUtilities.invokeLater(() -> keyHandler.onRightButton());
                        }

                        else if(event.getPin().getPin().equals(pin3)) {
                            SwingUtilities.invokeLater(() -> keyHandler.onBackButton());
                        }
                    }
                    else {
                        pinLow();
                    }
                }
            });
        }

    }

    private void pinHigh()
    {
//        java.lang.System.out.println(
//                Thread.currentThread().getName() + " - button - Alive: " +
//                        Thread.currentThread().isAlive());
//        SwingUtilities.invokeLater(() -> {
//            java.lang.System.out.println(
//                    Thread.currentThread().getName() + " - presssed highsss - Alive: " +
//                            Thread.currentThread().isAlive());
//                }
//
//        );
    }

    private void pinLow()
    {
     //   SwingUtilities.invokeLater(() -> keyHandler.onButtonReleased());
    }

    // forcefully shutdown all GPIO monitoring threads and scheduled tasks
    public void gpioShutdown()
    {
        gpio.shutdown();
    }

}
