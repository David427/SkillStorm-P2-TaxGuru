import { Link } from "react-router-dom";

import {
  Title,
  Header,
  PrimaryNav,
  NavMenuButton,
} from "@trussworks/react-uswds";
import { useState } from "react";

const navItems = [
  <Link to="/login" className="usa-nav__link">
    Log In
  </Link>,
  <Link to="/signup" className="usa-nav__link">
    Sign Up
  </Link>,
];

export function HeaderNav() {
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
          items={navItems}
          mobileExpanded={mobileExpanded}
          onToggleMobileNav={() => setMobileExpanded((prev) => !prev)}
        ></PrimaryNav>
      </div>
    </Header>
  );
}
