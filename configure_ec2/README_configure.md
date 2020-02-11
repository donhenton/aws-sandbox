# Installation on EC2

This is a list of all the actions needed to install spring boot birt on a t2.microinstance
<https://github.com/donhenton/spring-boot-birt>

## install docker

```bash
sudo yum update
sudo yum install -y docker
sudo usermod -a -G docker ec2-user
sudo curl -L https://github.com/docker/compose/releases/download/1.21.0/docker-compose-`uname -s`-`uname -m` | sudo tee /usr/local/bin/docker-compose > /dev/null
sudo chmod +x /usr/local/bin/docker-compose
# not needed sudo service docker start
sudo chkconfig docker on
```

## install postgres 11

<https://installvirtual.com/install-postgresql-11-on-amazon-linux-compile/>

sudo ./configure --without-readline --without-zlib
sudo make
// su
make install
sudo adduser postgres
sudo mkdir /usr/local/pgsql/data
sudo chown postgres /usr/local/pgsql/data
su - postgres
/usr/local/pgsql/bin/initdb -D /usr/local/pgsql/data
/usr/local/pgsql/bin/postgres -D /usr/local/pgsql/data >logfile 2>&1 &
/usr/local/pgsql/bin/createdb test
/usr/local/pgsql/bin/psql test

## Add postgres to path

```PATH=$PATH:$HOME/.local/bin:$HOME/bin:/usr/local/pgsql/bin```
in .bash_profile
source .bash_profile

## Load data to the database

* grant the ec2 instance a role that can read s3 buckets
* create a bucket and load in the jdatabase_file from <https://github.com/donhenton/postgres-sandbox/tree/master/jdatabase_backup> instructions there as well
* alter the owner of the jdatabase to the super user
* \dt to list tables may not work because the particular user doesn't have permissions on public schema
* the test user is only needed to run the script, and may be dropped if owner is altered

## Pulling on EC2

* ec2 service role must have AmazonEC2ContainerRegistryReadOnly
* ```sudo service docker start```
* ```$(aws ecr get-login --no-include-email --region us-east-2)``` log in
* ```docker pull 235926060045.dkr.ecr.us-east-2.amazonaws.com/spring-boot-birt:latest``` pull

## Login into postgres

* ```psql -h bd1hu143qs3ptvf.cnmr4n5eiz45.us-east-2.rds.amazonaws.com -U postgresuser -d postgres```

## Docker Compose for running the app on the instance

The public port is 80, the internal docker port is 9000. 9000 IS NOT visible to the outside world.

```yml
version: '3.1'
services:
    boot:
        container_name: boot
        ports:
            - "80:9000"
        image: 235926060045.dkr.ecr.us-east-2.amazonaws.com/spring-boot-birt:latest
        environment:
            - DATABASE_URL=postgres://postgresuser:postgres@bd1hu143qs3ptvf.cnmr4n5eiz45.us-east-2.rds.amazonaws.com:5432/jdatabase
            - spring.profiles.active=aws
```
