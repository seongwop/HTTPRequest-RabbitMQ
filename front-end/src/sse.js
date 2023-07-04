import React, { useEffect, useState } from "react";
import useUpdateEffect from './useUpdateEffect';

function Sse() {
  const [listening, setListening] = useState(false);
  const [data, setData] = useState([]);
  const [value, setValue] = useState(null);

  const [meventSource, msetEventSource] = useState(undefined);

  let eventSource = undefined;

  useEffect(() => {

    if (!listening) {
      eventSource = new EventSource("http://localhost:8080/connect"); // subscribe

      //msetEventSource(new EventSource("http://localhost:8088/connect"));

      msetEventSource(eventSource);

      // Custom listener
      // eventSource.addEventListener("Progress", (event) => {
      //   const result = JSON.parse(event.data);
      //   console.log("received:", result);
      //   setData(result)
      // });

      // when connected
      eventSource.onopen = event => {
        console.log("connection opened");
      };

      // when receive message
      eventSource.onmessage = event => {
        setData(old => [...old, event.data]);
        setValue(event.data);
      };

      // when errored
      eventSource.onerror = event => {
        console.log(event.target.readyState);
        if (event.target.readyState === EventSource.CLOSED) {
          console.log("eventsource closed (" + event.target.readyState + ")");
        }
        eventSource.close();
      };

      setListening(true);
    }

    return () => {
      eventSource.close();
      console.log("eventsource closed");
    };
  }, []);

  useUpdateEffect(() => {
    console.log("data: ", data);
  }, [data]);

  return (
    <div className="App">
      <header className="App-header">
        <div style={{ backgroundColor: "black" }}>
          Received UUID
          {data.map((d, index) => (
            <span key={index}>
            <br />
            {d}
          </span>
          ))}
        </div>
      </header>

    </div>
  );
}

export default Sse;