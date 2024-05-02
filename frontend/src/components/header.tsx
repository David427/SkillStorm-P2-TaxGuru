import { useState } from "react";
import { Link } from "react-router-dom";

import {
  Title,
  Header,
  PrimaryNav,
  NavMenuButton,
  LanguageSelector,
} from "@trussworks/react-uswds";
import { changeLanguage } from "i18next";

const navItems = [
  <Link to="/login" className="usa-nav__link">
    Log In
  </Link>,
  <Link to="/signup" className="usa-nav__link">
    Sign Up
  </Link>,
  <LanguageSelector
    className="usa-button--unstyled usa-nav__link language-selector"
    label="Languages"
    langs={[
      {
        attr: "en",
        label: "English",
        label_local: "English",
        on_click() {
          changeLanguage("es");
        },
      },
      {
        attr: "es",
        label: "Espanol",
        label_local: "Spanish",
        on_click() {
          changeLanguage("en");
        },
      },
    ]}
  />,
];

const authenticatedNavItems = [
  <Link to="/filing/personal" className="usa-nav__link">
    Begin Filing
  </Link>,
  <Link to="/account" className="usa-nav__link">
    My Account
  </Link>,
  <LanguageSelector
    className="usa-button--unstyled usa-nav__link language-selector"
    label="Languages"
    langs={[
      {
        attr: "en",
        label: "English",
        label_local: "English",
        on_click() {
          changeLanguage("es");
        },
      },
      {
        attr: "es",
        label: "Espanol",
        label_local: "Spanish",
        on_click() {
          changeLanguage("en");
        },
      },
    ]}
  />,
];

export function HeaderNav() {
  const isAuthenticated = false;
  const [mobileExpanded, setMobileExpanded] = useState(false);

  return (
    <Header basic={true} showMobileOverlay={mobileExpanded}>
      <div className="usa-nav-container">
        <div className="usa-navbar w-full">
          <Link to="/">
            <Title>TaxGuru</Title>
          </Link>

          <NavMenuButton
            onClick={() => setMobileExpanded((prev) => !prev)}
            label="Menu"
          />
        </div>

        <PrimaryNav
          items={isAuthenticated ? authenticatedNavItems : navItems}
          mobileExpanded={mobileExpanded}
          onToggleMobileNav={() => setMobileExpanded((prev) => !prev)}
        ></PrimaryNav>
      </div>
    </Header>
  );
}
