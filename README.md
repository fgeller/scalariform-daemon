# scalariform-daemon
Trigger formatting of files via HTTP requests.

### Build

    sbt assembly

### Run

    java -jar ./target/scala-2.11/scalariform-daemon.jar

or

    sbt run

### Use

Make an HTTP request against localhost and provide the `fileName` and `preferencesFile` parameters like:

    http://127.0.0.1:5474/format?fileName=/path/to/your/file&preferencesFile=/path/to/your/preferences/file

For example, in my Emacs config I have the following call added to
`before-save-hook` in Scala buffers, where `scalariform-preferences-file` is a
directory local variable I set in my Scala projects:

    (defun scalariform-daemon-format-file ()
      (when (and (boundp 'scalariform-preferences-file) scalariform-preferences-file)
        (request
         "http://127.0.0.1:5474/format"
         :params `((fileName . ,buffer-file-name)
                   (preferencesFile . ,scalariform-preferences-file)))))


