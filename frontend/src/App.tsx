import { useTranslation } from "react-i18next";

import { Link } from "react-router-dom";
import { Grid, GridContainer } from "@trussworks/react-uswds";

function App() {
  const { t } = useTranslation();

  return (
    <main className="full-page">
      <section className="usa-hero">
        <GridContainer>
          <div className="usa-hero__callout">
            <h1 className="usa-hero__heading">
              <span className="usa-hero__heading--alt">
                {t("index.hero-heading-alt")}
              </span>
              {t("index.hero-heading")}
            </h1>
            <p>{t("index.hero-desc")}</p>
            <Link to="/signup" className="usa-button">
              {t("index.hero-cta")}
            </Link>
          </div>
        </GridContainer>
      </section>
      <section className="grid-container usa-section">
        <Grid row gap>
          <Grid tablet={{ col: 4 }}>
            <h2 className="font-heading-xl margin-top-0">
              {t("index.tagline")}
            </h2>
          </Grid>
          <Grid tablet={{ col: 8 }} className="usa-prose">
            <p>{t("index.point-1")}</p>
            <p>{t("index.point-2")}</p>
          </Grid>
        </Grid>
      </section>
    </main>
  );
}

export default App;
