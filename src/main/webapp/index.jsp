<%@page pageEncoding="UTF-8" %>
<html>
<title>Выравнивание</title>
<style type="text/css">
    TABLE {
        width: 100%; /* Ширина таблицы */
        height: 100%; /* Высота таблицы */
    }
</style>
<body>
<script type="text/javascript">
    console.log("js")

    //var url = "http://localhost:8080/rpi/log"
    var url = "http://zzuummaa.sytes.net:8070/rpi/log"

    var lastRecord = -1

    function clearHistory() {
        document.getElementById("log-ta").value = ""

        var xhr = new XMLHttpRequest();

        xhr.onreadystatechange = function() {
            if (this.readyState != 4) return;

            if (this.status == 200) {
                console.log("clear history success")
            } else {
                console.log("clear history error")
                onError(this);
            }
        }

        xhr.open("GET", url + "?clear_history=true", true);
        xhr.send();
    }

    function onMessage(text) {
        var message = JSON.parse(text);

        if (message.hasOwnProperty("log_data")) {
            document.getElementById("log-ta").value += message.log_data;
        }

        if (message.hasOwnProperty("last_record")) {
            lastRecord = message.last_record
        }
    }

    function onError(xhr) {
        alert(xhr.statusText);
    }

    function subscribe() {
        //console.log("on subscribe")

        var xhr = new XMLHttpRequest();

        xhr.onreadystatechange = function() {
            if (this.readyState != 4) return;

            if (this.status == 200) {
                //console.log("onMessage");
                onMessage(this.responseText);
                subscribe()
            } else {
                console.log("onError")
                onError(this);
                setTimeout(5000, subscribe)
            }
        }

        xhr.open("GET", url + "?last_record=" + lastRecord, true);
        xhr.send();
    }
    subscribe()
</script>
<table>
    <tr>
        <td align="center">
            <textarea id="log-ta" cols="80" rows="20" readonly></textarea>
        </td>
    </tr>
    <tr>
        <td align="center">
            <input type="button" onclick="clearHistory()" value="Очистить историю"/>
        </td>
    </tr>
    <tr></tr>
</table>
</body>
</html>
