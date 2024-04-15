import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.css";

import "@trussworks/react-uswds/lib/uswds.css";
import "@trussworks/react-uswds/lib/index.css";

import { BrowserRouter, Route, Routes } from "react-router-dom";

import Login from "./pages/login.tsx";
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
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
