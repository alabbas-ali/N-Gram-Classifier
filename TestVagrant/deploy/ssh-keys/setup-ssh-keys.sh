#!/usr/bin/env bash

# allow agent pipe re-use
echo "Defaults env_keep += \"SSH_AUTH_SOCK\"" >> /etc/sudoers.d/vagrant

su vagrant
    # allow access only with the developer's key
    cat /tmp/vagrant/setup_ssh_keys/id_rsa.pub > /home/vagrant/.ssh/authorized_keys

    getent hosts git.fra1.internal | awk '{ print $1 }' | xargs -P1 -I % ssh-keyscan % >> /home/vagrant/.ssh/known_hosts
    getent hosts git.fra9.internal | awk '{ print $1 }' | xargs -P1 -I % ssh-keyscan % >> /home/vagrant/.ssh/known_hosts
    ssh-keyscan git.fra1.internal >> /home/vagrant/.ssh/known_hosts
    ssh-keyscan git.fra9.internal >> /home/vagrant/.ssh/known_hosts
exit

# clean up
rm -rf /tmp/vagrant/setup_ssh_keys
