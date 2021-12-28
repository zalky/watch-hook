
# Watch Hook Example Repo

The purpose of this repo is to demonstrate a minimal example of how
you might reactively update a JS object that is based on runtime style
data, in response to changes in CSS.

The main motivation is avoid duplicating style definitions across CSS
and client code.

For our example we use the
[Echarts](https://echarts.apache.org/en/index.html) library, which
renders charts on an HTML canvas. Chart configuration data, including
styles, are specified via JS objects at runtime. To avoid duplicating
style definitions across CSS and client code, we embed CSS properties
on the root element and retrieve them at runtime.

The two relevant files are:

- `resources/css/main.css`
- `src/build/client.cljs`

While the client app is reloaded on cljs code changes, you will notice
that changes only to the CSS are not automatically propagated to the
rendered chart. One solution would be to introduce a hook that runs
after the ```{:devtools {:watch-dir "assets/folder"}}``` watcher loads
css.

To simulate what this would look like, modify the following property
definition in `main.css` to a different colour:

```css
    --primary-color: #0f0; /* -> #f00; */
```

Then click the `Simulate watch hook` button in the browser above the
chart. The chart should update reactively.

## Running

First install the npm dependencies which include Echarts, a JS library
that requires a JSON style spec:

```sh
npm install
```

Run shadow-cljs with the built-in http web server:

```sh
clj -M:cljs
```

Visit the main page in the browser:

```sh
http://localhost:8000/index.html
```

