/*
 * SOURCE FILE GENERATATED BY JACOB CHANNEL CLASS GENERATOR
 * 
 *               !!! DO NOT EDIT !!!! 
 * 
 * Generated On  : Sat Dec 05 03:48:07 UTC 2015
 * For Interface : org.apache.ode.bpel.runtime.channels.ActivityRecovery
 */

package org.apache.ode.bpel.runtime.channels;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * An auto-generated channel listener abstract class for the 
 * {@link org.apache.ode.bpel.runtime.channels.ActivityRecovery} channel type. 
 * @see org.apache.ode.bpel.runtime.channels.ActivityRecovery
 * @see org.apache.ode.bpel.runtime.channels.ActivityRecoveryChannel
 */
public abstract class ActivityRecoveryChannelListener
    extends org.apache.ode.jacob.ChannelListener<org.apache.ode.bpel.runtime.channels.ActivityRecoveryChannel>
    implements org.apache.ode.bpel.runtime.channels.ActivityRecovery
{

    private static final Logger __log = LoggerFactory.getLogger(org.apache.ode.bpel.runtime.channels.ActivityRecovery.class);

    protected Logger log() { return __log; } 

    protected ActivityRecoveryChannelListener(org.apache.ode.bpel.runtime.channels.ActivityRecoveryChannel channel) {
       super(channel);
    }
}
