import {
  Form,
  Grid,
  Label,
  Button,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

export default function Review() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleReviewInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    navigate("/filing/results");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="complete" />
          <StepIndicatorStep label={t("deductions.title")} status="complete" />
          <StepIndicatorStep label={t("review.title")} status="current" />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        <Form onSubmit={handleReviewInfo} className="w-full">
          <Fieldset legend={t("review.desc")}>
            {/* Personal */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/personal">{t("personal.title")}</Link>
              </h4>
              {/* Fname & Lname */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-first_name" htmlFor="first_name">
                    {t("personal.fname")}
                  </Label>
                  <TextInput
                    id="first_name"
                    name="first_name"
                    aria-labelledby="label-first_name"
                    type="text"
                    defaultValue="First Name"
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-last_name" htmlFor="last_name">
                    {t("personal.lname")}
                  </Label>
                  <TextInput
                    id="last_name"
                    name="last_name"
                    aria-labelledby="label-last_name"
                    type="text"
                    defaultValue="Last Name"
                    disabled
                  />
                </Grid>
              </Grid>

              {/* Birthdate & SSN */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-birthdate" htmlFor="birthdate">
                    {t("personal.birthdate")}
                  </Label>
                  <TextInputMask
                    id="birthdate"
                    name="birthdate"
                    type="text"
                    aria-labelledby="label-birthdate"
                    aria-describedby="hint-birthdate"
                    mask="__/__/____"
                    // the pattern fits the date format MM/DD/YYYY
                    pattern="^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{4}$"
                    defaultValue="11/28/2001"
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-ssn" htmlFor="ssn">
                    SSN or TIN
                  </Label>
                  <TextInputMask
                    id="ssn"
                    name="ssn"
                    type="text"
                    aria-labelledby="label-ssn"
                    mask="___-__-____"
                    pattern="\d{3}-\d{2}-\d{4}"
                    defaultValue="123-45-6789"
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Filing Information */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/filing-information">
                  {t("filing-info.title")}
                </Link>
              </h4>

              {/* Filing Status and Dependents */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-filing_status" htmlFor="filing_status">
                    {t("filing-info.status")}
                  </Label>
                  <TextInput
                    id="filing_status"
                    name="filing_status"
                    type="text"
                    aria-labelledby="label-filing_status"
                    defaultValue={"Single"}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-dependents" htmlFor="dependents">
                    {t("review.dependents")}
                  </Label>
                  <TextInput
                    id="dependents"
                    name="dependents"
                    type="number"
                    aria-labelledby="label-dependents"
                    defaultValue={0}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* W2 */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/w2">{t("w2.title")}</Link>
              </h4>

              {/* Employer Name, Wages, & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-employer_name" htmlFor="employer_name">
                    {t("w2.ename")}
                  </Label>
                  <TextInput
                    id="employer_name"
                    name="employer_name"
                    type="text"
                    aria-labelledby="label-employer_name"
                    defaultValue={"Skillstorm Commercial Services, LLC"}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-wages" htmlFor="wages">
                    {t("review.wages")}
                  </Label>
                  <TextInput
                    id="wages"
                    name="wages"
                    type="number"
                    aria-labelledby="label-wages"
                    defaultValue={100_000}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-w2_withheld" htmlFor="w2_withheld">
                    {t("w2.federal-withheld")}
                  </Label>
                  <TextInput
                    id="w2_withheld"
                    name="w2_withheld"
                    type="number"
                    aria-labelledby="label-w2_withheld"
                    defaultValue={10_000}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Self Employment */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/self-employment">{t("1099.title")}</Link>
              </h4>

              {/* Non-Employment Compensation & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-nec_wages" htmlFor="nec_wages">
                    {t("1099.nec")}
                  </Label>
                  <TextInput
                    id="nec_wages"
                    name="nec_wages"
                    type="number"
                    aria-labelledby="label-nec_wages"
                    defaultValue={10_000}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-nec_withheld" htmlFor="nec_withheld">
                    {t("w2.federal-withheld")}
                  </Label>
                  <TextInput
                    id="nec_withheld"
                    name="nec_withheld"
                    type="number"
                    aria-labelledby="label-nec_withheld"
                    defaultValue={1_000}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Deductions */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/deductions">{t("deductions.title")}</Link>
              </h4>
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label
                    id="label-mortgage-interest"
                    htmlFor="mortgage_interest"
                  >
                    {t("deductions.interest")}
                  </Label>
                  <TextInput
                    id="mortgage_interest"
                    name="mortgage_interest"
                    type="number"
                    aria-labelledby="label-mortgage-interest"
                    defaultValue={0}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-property-taxes" htmlFor="property_taxes">
                    {t("deductions.taxes")}
                  </Label>
                  <TextInput
                    id="property_taxes"
                    name="property_taxes"
                    type="number"
                    aria-labelledby="label-property-taxes"
                    defaultValue={0}
                    disabled
                  />
                </Grid>
              </Grid>

              {/* Donations */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-donations" htmlFor="donations">
                    {t("deductions.donations")}
                  </Label>
                  <TextInput
                    id="donations"
                    name="donations"
                    type="number"
                    aria-labelledby="label-donations"
                    defaultValue={0}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label
                    id="label-nonCash-donations"
                    htmlFor="nonCash_donations"
                  >
                    {t("deductions.nonCash")}
                  </Label>
                  <TextInput
                    id="nonCash_donations"
                    name="nonCash_donations"
                    type="number"
                    aria-labelledby="label-nonCash-donations"
                    defaultValue={0}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Button type="submit">{t("continue")}</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
