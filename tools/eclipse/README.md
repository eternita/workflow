Neuro4j Studio Installer
=================

Simple installation script for Neuro4j Studio (Linux only).


Based on https://github.com/bepcyc/eclipse_installer - Thank you!

Just download the .zip distribution of Studio from http://static.neuro4j.org/download/studio/.


INSTALL
=======

Save file   n4jstudio_installer.sh

RUN
===

Chmod if you need it:

```bash
chmod +x n4jstudio_installer.sh
```

Put downloaded .zip file into the same directory as script.
Run the script:

```bash
./n4jstudio_installer.sh
```

or

```bash
./n4jstudio_installer.sh --quiet
```

If you don't want it to be verbose.

Run the Studio:

```bash
/usr/bin/n4jstudio -clean &
```



