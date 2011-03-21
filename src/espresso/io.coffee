# file i/o library

class File
  constructor: (name) ->
    @file = new java.io.File(name)

    # properties
    @.name = name
    @.is_dir = @file.isDirectory()

  exists: -> @file.exists()

  to_s: ->
    Packages.espresso.io.FileIO.read(@file)

  to: (dest) ->
    Packages.espresso.io.FileIO.copy(new java.io.FileInputStream(@file), dest.__out())

  # internal method
  __out: ->
    new java.io.FileOutputStream(@file)

  # dir traversal
  each: (callback) ->
    if not @.is_dir
      throw "'#{@file.name}' is not a directory"

    iter = Packages.espresso.io.FileIO.list(@file)
    keep_going = true
    while iter.hasNext() and keep_going
      keep_going = callback iter.next()


class Url
  constructor: (url) ->
    @url = url

  to_s: ->
    string = new java.lang.StringWriter()
    Packages.espresso.io.FileIO.copy(new java.net.URL(@url).openStream(), string)
    string

  to: (dest) ->
    Packages.espresso.io.FileIO.copy(new java.net.URL(@url).openStream(), dest.__out())

class Process
  constructor: (command) ->
    @command = command
    @process = java.lang.Runtime.getRuntime().exec(@command)

  __out: ->
    @process

  to_s: ->
    string = new java.lang.StringWriter()
    Packages.espresso.io.FileIO.copy(@process.getInputStream(), string)
    string

  to: (dest) ->
    Packages.espresso.io.FileIO.copy(@process.getInputStream(), dest.__out())

  close: ->
    @process.waitFor()


global.io =
  file: (name) ->
    new File(name)

  url: (url) ->
    new Url(url)

  run: (command) ->
    new Process(command)