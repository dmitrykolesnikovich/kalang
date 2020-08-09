<!DOCTYPE html>
<html>
  <head>
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="chrome=1">

    <link rel="stylesheet" type="text/css" href="stylesheets/stylesheet.css" media="screen">
    <link rel="stylesheet" type="text/css" href="stylesheets/github-dark.css" media="screen">

    <title><#if (content.title)??><#escape x as x?xml>${content.title}</#escape><#else></#if></title>
  </head>

  <body>

    <header>
      <div class="container">
        <h1><#if (content.title)??><#escape x as x?xml>${content.title}</#escape><#else></#if></h1>
        <h2></h2>

        <section id="downloads">
          <a href="${config.site_host}" class="btn btn-github"><span class="icon"></span>View on GitHub</a>
        </section>
      </div>
    </header>

    <div class="container">
      <section id="main_content">
        ${content.body}
      </section>
    </div>

    <footer>
      <div class="container">
        Copyright©2020 <a href="https://github.com/kasonyang">Kason Yang</a>
      </div>
    </footer>

  </body>
</html>