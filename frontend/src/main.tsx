import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.css";

import "@trussworks/react-uswds/lib/uswds.css";
import "@trussworks/react-uswds/lib/index.css";

import { BrowserRouter, Route, Routes } from "react-router-dom";

import Login from "./pages/login.tsx";
import W2 from "./pages/filing/w2.tsx";
import SignUp from "./pages/signup.tsx";
import Personal from "./pages/filing/personal.tsx";
import { HeaderNav } from "./components/header.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <HeaderNav />
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/filing/personal" element={<Personal />} />
        <Route path="/filing/w2" element={<W2 />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
