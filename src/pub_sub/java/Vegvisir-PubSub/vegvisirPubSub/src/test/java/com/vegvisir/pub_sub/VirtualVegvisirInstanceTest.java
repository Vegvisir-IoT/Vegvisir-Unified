package com.vegvisir.pub_sub;



import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VirtualVegvisirInstanceTest {
    @Test
     void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void test_virtual_instance(){
        VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance(  );
        assertTrue( virtual != null );
    }

    @Test
    void getInstance(){
        VirtualVegvisirInstance a = VirtualVegvisirInstance.getInstance();
        assertEquals( "DeviceA", a.getDeviceId() );
        VirtualVegvisirInstance b = VirtualVegvisirInstance.getInstance("Danny");
        assertEquals("Danny", b.getDeviceId() );
        assertNotEquals( a, b );
    }

    @Test
    void updateSubscriptionList(){
        VegvisirApplicationContext context = new VegvisirApplicationContext( "Shop",
                "We all should shop more",
                Stream.of("Pepsi", "Coke").collect(Collectors.toSet()));
        VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance( "DeviceA" );
        VirtualVegvisirApplicationDelegator delegator = new VirtualVegvisirApplicationDelegator();

        virtual.registerApplicationDelegator( context, delegator );


    }





            // context unused so there's a problem with the update of several subscribers to a channel

}