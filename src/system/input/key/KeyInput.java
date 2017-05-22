package system.input.key;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.util.CommandArgumentParser;

import java.util.ArrayList;

/**
 * Created by Erwin on 5/15/2017.
 */
class KeyInput {

    // Key handler to pass the events through when a pin changes its state
    IKeyHandler keyHandler;


    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();


    public void KeyInput(IKeyHandler keyHandler) throws InterruptedException, PlatformAlreadyAssignedException {
        this.keyHandler = keyHandler;

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
                    if (event.getState().isHigh()) {
                        pinHigh();
                    }
                    else pinLow();
                }
            });
        }

    }

    private void pinHigh()
    {
        keyHandler.onButtonPressed();
    }

    private void pinLow()
    {
        keyHandler.onButtonReleased();
    }

    // forcefully shutdown all GPIO monitoring threads and scheduled tasks
    public void gpioShutdown()
    {
        gpio.shutdown();
    }

}
