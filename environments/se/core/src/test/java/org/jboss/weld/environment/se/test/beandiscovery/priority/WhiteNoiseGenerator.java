package org.jboss.weld.environment.se.test.beandiscovery.priority;

public class WhiteNoiseGenerator implements SoundSource {

    @Override
    public String generateSound() {
        return "ssssssssssss";
    }

}
