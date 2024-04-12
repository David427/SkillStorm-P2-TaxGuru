import { Grid } from "@trussworks/react-uswds";
import { Link } from "react-router-dom";

function App() {
  return (
    <main className="full-page">
      <section className="usa-hero">
        <div className="usa-hero__callout">
          <h1 className="usa-hero__heading">
            <span className="usa-hero__heading--alt">Act now:</span>
            Federal taxes are due by April 15th
          </h1>
          <p>
            We'll walk you through our free federal tax tool, to ensure you
            complete your taxes on time and with ease.
          </p>
          <Link to="/signup" className="usa-button">
            Get Started Today
          </Link>
        </div>
      </section>
      <section className="grid-container usa-section">
        <Grid row gap>
          <Grid tablet={{ col: 4 }}>
            <h2 className="font-heading-xl margin-top-0">
              File Your Taxes For Free
            </h2>
          </Grid>
          <Grid tablet={{ col: 8 }} className="usa-prose">
            <p>
              TaxGuru is your reliable companion for filing your federal taxes
              effortlessly. Say goodbye to the stress of tax season and embrace
              a seamless experience with our platform.
            </p>
            <p>
              Our platform uses the latest IRS guidelines to calculate your
              returns with precision, ensuring you get every dollar you're
              entitled to, and providing you with peace-of-mind.
            </p>
          </Grid>
        </Grid>
      </section>
    </main>
  );
}

export default App;
