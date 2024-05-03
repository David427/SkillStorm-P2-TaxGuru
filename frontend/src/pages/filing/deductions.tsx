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
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

export default function Deductions() {
  const { t } = useTranslation();

  const navigate = useNavigate();

  const handleDeductions = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    navigate("/filing/review");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="complete" />
          <StepIndicatorStep label={t("deductions.title")} status="current" />
          <StepIndicatorStep label={t("review.title")} />
          <StepIndicatorStep label={t("results.title")} />
        </StepIndicator>

        <Form onSubmit={handleDeductions} className="w-full">
          <Fieldset legend={t("deductions.desc")}>
            {/* Mortgage Interest & Property Taxes Paid */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-mortgage-interest" htmlFor="mortgage_interest">
                  {t("deductions.interest")}
                </Label>
                <TextInput
                  id="mortgage_interest"
                  name="mortgage_interest"
                  type="number"
                  aria-labelledby="label-mortgage-interest"
                  defaultValue={0}
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
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-nonCash-donations" htmlFor="nonCash_donations">
                  {t("deductions.nonCash")}
                </Label>
                <TextInput
                  id="nonCash_donations"
                  name="nonCash_donations"
                  type="number"
                  aria-labelledby="label-nonCash-donations"
                  defaultValue={0}
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Link
                  to="/filing/review"
                  className="usa-button usa-button--base"
                >
                  {t("skip")}
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
