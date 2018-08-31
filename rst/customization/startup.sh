#!/bin/bash
sed -i "s|DB_USER|$DBUSER|g" $CUSTOMIZATION/commands.cli 
sed -i "s|DB_PASSWORD|$DBPASSWORD|g" $CUSTOMIZATION/commands.cli 
sed -i "s|DB_HOST|$DBHOST|g" $CUSTOMIZATION/commands.cli 
sed -i "s|DB_PORT|$DBPORT|g" $CUSTOMIZATION/commands.cli
sed -i "s|DB_NAME|$DBNAME|g" $CUSTOMIZATION/commands.cli
sed -i "s|DS_JNDI_NAME|$JNDINAME|g" $CUSTOMIZATION/commands.cli
sh $CUSTOMIZATION/execute.sh
mv $CUSTOMIZATION/*.ear /opt/jboss/wildfly/standalone/deployments/
sh /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0

