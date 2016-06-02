# polarusbdump

#### USB data dumper for Polar M400 (and other) watches.

I started this project as an attempt to write a polar watch data extractor and converter 
to TCX format. Right now I just want this to be a simple data dumper, as I figured out
how to dump data over Bluetooth and will focus on that project.

#### Compile

`mvn clean package`

#### Run

`java -jar target/polarusbdump-1.0-SNAPSHOT.jar`

#### Data

All data will be dumped into `${user.home}/.polar/backup/${device.id}/` directory. Please
note that this data is still in polar format (Google Procol Buffers encoded).